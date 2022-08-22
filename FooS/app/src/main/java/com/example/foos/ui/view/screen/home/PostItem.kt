package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.component.UserIcon

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
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        PostItemContent(
            uiState,
            onUserIconClick,
            onContentClick,
            onImageClick
        )
        ReactionRow(modifier = Modifier.fillMaxWidth())
    }
}

/**
 * 投稿内容の部分
 * @param uiState UI状態
 * @param onContentClick コンテンツクリック時の挙動
 * @param onImageClick 画像クリック時の挙動
 */
@Composable
fun PostItemContent(
    uiState: PostItemUiState,
    onUserIconClick: (userId: String) -> Unit = { },
    onContentClick: (PostItemUiState) -> Unit = { },
    onImageClick: (PostItemUiState, String) -> Unit = { _, _ -> },
) {
    Row(
        modifier = Modifier.clickable {
            onContentClick.invoke(uiState)
        }
    ) {
        UserIcon(url = uiState.userIcon, onClick = { onUserIconClick.invoke(uiState.userId) })
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            UserIdentityRow(uiState.username, uiState.userId)
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
 */
@Composable
fun UserIdentityRow(
    username: String,
    userId: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text(text = "@${userId}", maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * 添付画像の水平方向リスト
 * @param images 添付画像のURL
 * @param onClick 各画像クリック時の挙動
 */
@Composable
fun AttachedImagesRow(
    uiState: PostItemUiState,
    onClick: (PostItemUiState, String) -> Unit = { _, _ -> }
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
