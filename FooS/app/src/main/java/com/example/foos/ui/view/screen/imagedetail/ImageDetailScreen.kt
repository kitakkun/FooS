package com.example.foos.ui.view.screen.imagedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl

/**
 * 画像を全画面プレビューするスクリーン
 * 投稿内容の画像をタップした時などに遷移
 */
@Composable
fun ImageDetailScreen(navController: NavHostController, post: PostItemUiStateWithImageUrl?) {
    post?.let {
        LazyRow()
        {
            items(post.uiState.attachedImages) {
                FullSizeImage(url = it)
            }
        }
    }
}

@Composable
fun FullSizeImage(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url).build(),
        contentDescription = null,
        modifier = modifier.fillMaxSize()
    )
}