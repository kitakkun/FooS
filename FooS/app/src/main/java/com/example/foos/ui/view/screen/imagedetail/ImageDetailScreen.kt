package com.example.foos.ui.view.screen.imagedetail

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.foos.ui.view.component.MaxSizeLoadingIndicator
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch

/**
 * 画像を全画面プレビューするスクリーン
 * 投稿内容の画像をタップした時などに遷移
 */
@Composable
fun ImageDetailScreen(
    navController: NavHostController,
    imageUrls: List<String>,
    initialIndex: Int
) {
    val lazyListState = rememberLazyListState()
    var showedFirstTime by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (showedFirstTime) {
            coroutineScope.launch {
                lazyListState.scrollToItem(initialIndex)
                showedFirstTime = false
            }
        }
    }

    ImageDetailUI(
        lazyListState = lazyListState,
        imageUrls = imageUrls,
        onBack = { navController.navigateUp() })
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun ImageDetailUI(
    lazyListState: LazyListState,
    imageUrls: List<String>,
    onBack: () -> Unit
) {
    Box {
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(imageUrls) {
                Log.d("TAG", it)
                FullSizeImage(url = it, modifier = Modifier.fillParentMaxSize())
            }
        }
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            backgroundColor = Color.Transparent.copy(alpha = 0.1f),
        )
    }
}

/**
 * フルスクリーン画像
 * @param url 画像のパス
 * @param modifier モディファイア
 */
@Composable
private fun FullSizeImage(
    url: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            loading = {
                MaxSizeLoadingIndicator()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ImageDetailUIPreview() {
    ImageDetailUI(lazyListState = rememberLazyListState(), imageUrls = listOf(), onBack = {})
}
