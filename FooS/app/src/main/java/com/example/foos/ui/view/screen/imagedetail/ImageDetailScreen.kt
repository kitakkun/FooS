package com.example.foos.ui.view.screen.imagedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

/**
 * 画像を全画面プレビューするスクリーン
 * 投稿内容の画像をタップした時などに遷移
 */
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun ImageDetailScreen(navController: NavHostController, post: PostItemUiStateWithImageUrl?) {
    post?.let {
        val lazyListState = rememberLazyListState()
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
            modifier = Modifier.fillMaxSize()
        )
        {
            items(post.uiState.attachedImages) {
                FullSizeImage(url = it, modifier = Modifier.fillParentMaxSize())
            }
        }
    }
}

/**
 * フルスクリーン画像
 * @param url 画像のパス
 * @param modifier モディファイア
 */
@Composable
fun FullSizeImage(
    url: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .placeholder(R.drawable.ic_no_image)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}