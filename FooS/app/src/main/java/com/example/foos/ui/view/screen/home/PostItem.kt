package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.constants.FONT_SIZE_USERID
import com.example.foos.ui.constants.FONT_SIZE_USERNAME
import com.example.foos.ui.constants.PADDING_MEDIUM
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.component.UserIcon
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


/**
 * 投稿のリスト
 * @param uiStates 各投稿のUI状態
 * @param onUserIconClick ユーザアイコンクリック時の挙動
 * @param onContentClick コンテンツクリック時の挙動
 * @param onImageClick 添付画像クリック時の挙動
 */
@Composable
fun PostItemList(
    uiStates: List<PostItemUiState>,
    onUserIconClick: (userId: String) -> Unit = { },
    onContentClick: (uiState: PostItemUiState) -> Unit = { },
    onImageClick: (uiState: PostItemUiState, clickedImageUrl: String) -> Unit = { _, _ -> },
    headerContent: @Composable() () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            headerContent()
        }
        items(uiStates) { post ->
            PostItem(post, onUserIconClick, onContentClick, onImageClick)
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}

/**
 * タイムラインに表示する投稿アイテム一つに対応するコンポーザブル
 * @param uiState UI状態
 * @param onUserIconClick ユーザアイコンクリック時の挙動
 * @param onContentClick コンテンツクリック時の挙動
 * @param onImageClick 画像クリック時の挙動
 */
@Composable
fun PostItem(
    uiState: PostItemUiState,
    onUserIconClick: (userId: String) -> Unit = { },
    onContentClick: (PostItemUiState) -> Unit = { },
    onImageClick: (PostItemUiState, String) -> Unit = { _, _ -> },
) {
    Row(
        modifier = Modifier
            .clickable { onContentClick.invoke(uiState) }
            .padding(PADDING_MEDIUM)
    ) {
        UserIcon(url = uiState.userIcon, onClick = { onUserIconClick.invoke(uiState.userId) })
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            UserIdentityRow(uiState.username, uiState.userId, createdAt = uiState.createdAt)
            Text(
                text = uiState.content,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
            )
            Spacer(Modifier.height(16.dp))
            AttachedImagesRow(uiState, onImageClick)
        }
    }
}

/**
 * ユーザ情報の行アイテム
 * @param username ユーザ名
 * @param userId ユーザID
 * @param createdAt 投稿の作成日時
 */
@Composable
fun UserIdentityRow(
    username: String,
    userId: String,
    createdAt: Date?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = username, fontSize = FONT_SIZE_USERNAME, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "@${userId}",
                fontSize = FONT_SIZE_USERID,
                modifier = Modifier.weight(1f, fill = false),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            PostTime(createdAt = createdAt)
        }
    }
}

/**
 * 投稿時間の表示
 * @param createdAt 投稿の作成日時
 */
@Composable
fun PostTime(
    createdAt: Date?
) {
    createdAt?.let {
        val createdAtDateTime =
            createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val nowDateTime = LocalDateTime.now()
        val daysDiff = ChronoUnit.DAYS.between(createdAtDateTime, nowDateTime)
        val hoursDiff = ChronoUnit.HOURS.between(createdAtDateTime, nowDateTime)
        val minutesDiff = ChronoUnit.MINUTES.between(createdAtDateTime, nowDateTime)
        val secondsDiff = ChronoUnit.SECONDS.between(createdAtDateTime, nowDateTime)
        val text: String = if (daysDiff >= 7) {
            // ex) 9 Jan
            createdAtDateTime.format(DateTimeFormatter.ofPattern("d MMM"))
        } else if (daysDiff >= 1) {
            // ex) 5d
            "${daysDiff}d"
        } else if (hoursDiff >= 1) {
            // ex) 7h
            "${hoursDiff}h"
        } else if (minutesDiff >= 1) {
            // ex) 10m
            "${minutesDiff}m"
        } else {
            // ex) 40s
            "${secondsDiff}s"
        }
        Text("・${text}")
    }
}

/**
 * 添付画像の水平方向リスト
 * @param uiState 投稿アイテムのUI状態
 * @param onClick 各画像クリック時の挙動
 */
@Composable
fun AttachedImagesRow(
    uiState: PostItemUiState,
    onClick: (PostItemUiState, String) -> Unit = { _, _ -> },
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.attachedImages) { image ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_no_image)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onClick.invoke(uiState, image) }
                    .size(120.dp)
                    .clip(RoundedCornerShape(10)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
