package com.github.kitakkun.foos.customview.composable.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.composable.user.HorizontalUserIdentityText
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.FooSTheme
import java.util.*

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
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit,
    onImageClick: (List<String>, String) -> Unit,
    onMoreVertClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onContentClick.invoke(uiState.postId) }
            .padding(paddingMedium)
    ) {
        UserIcon(url = uiState.userIcon, onClick = { onUserIconClick.invoke(uiState.userId) })
        Spacer(modifier = Modifier.width(paddingMedium))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalUserIdentityText(
                    username = uiState.username,
                    userId = uiState.userId,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "・${uiState.formattedCreatedAtText}",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Light,
                )
                IconButton(onClick = { onMoreVertClick(uiState.postId) }, Modifier.size(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
            Text(
                text = uiState.content,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
            )
            Spacer(Modifier.height(paddingMedium))
            AttachedImagesRow(uiState.attachedImages, onImageClick)
        }
    }
}

@Preview
@Composable
private fun PostItemPreview() = PreviewContainer {
    val uiState = PostItemUiState(
        userIcon = "",
        username = "username",
        userId = "userIduserIduserIduserIduserIduserIduserIduserId",
        content = "some interesting post content...",
        attachedImages = listOf("", "", ""),
        postId = "",
        createdAt = Date()
    )
    FooSTheme {
        PostItem(
            uiState = uiState,
            onUserIconClick = {},
            onContentClick = {},
            onImageClick = { _, _ -> },
            onMoreVertClick = {},
        )
    }
}

/**
 * 添付画像の水平方向リスト
 * @param attachedImages 投稿アイテムのUI状態
 * @param onClick 各画像クリック時の挙動
 */
@Composable
fun AttachedImagesRow(
    attachedImages: List<String>,
    onClick: (List<String>, String) -> Unit = { _, _ -> },
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(paddingMedium)
    ) {
        items(attachedImages) { image ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                loading = {
                    MaxSizeLoadingIndicator()
                },
                contentDescription = null,
                modifier = Modifier
                    .clickable { onClick.invoke(attachedImages, image) }
                    .size(120.dp)
                    .clip(RoundedCornerShape(10)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
