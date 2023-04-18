package com.github.kitakkun.foos.post.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.const.paddingLarge
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.customview.composable.button.RoundIconActionButton
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.composable.post.PostItemList
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * ホーム画面のコンポーザブル。ユーザーの投稿をリストで表示。
 * @param viewModel スクリーンに対応するViewModel
 * @param navController 画面遷移用のNavController
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModelImpl = hiltViewModel(),
    navController: NavController,
) {

    val uiState = viewModel.uiState.value
    val listState = rememberLazyListState()

    DisposableEffect(FirebaseAuth.getInstance().currentUser) {
        onDispose {
            viewModel.dispose()
        }
    }

    LaunchedEffect(Unit) {
        launch {
            // 初回起動時の投稿フェッチ
            viewModel.fetchInitialPosts()
        }
        launch {
            viewModel.navEvent.collect {
                navController.navigate(it)
            }
        }
        launch {
            // Bottomナビゲーションでホームがクリックされたらトップへスクロール(いったんなし）
        }
    }

    HomeUI(
        uiState = uiState, listState = listState,
        onPostCreateButtonClick = {
            navController.navigate(PostScreenRouter.PostCreate.route)
        },
        isLoadingPosts = false, /* TODO: ロードインディケータの適切な制御 */
        onAppearLastItem = { viewModel.fetchOlderPosts() },
        onImageClick = { imageUrls, clickedUrl -> viewModel.onImageClick(imageUrls, clickedUrl) },
        onUserIconClick = { viewModel.onUserIconClick(it) },
        onRefresh = { viewModel.onRefresh() },
        onContentClick = { viewModel.onContentClick(it) },
        onMoreVertClick = viewModel::onMoreVertClick,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeUI(
    uiState: HomeScreenUiState,
    listState: LazyListState,
    isLoadingPosts: Boolean,
    onRefresh: () -> Unit,
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit,
    onImageClick: (List<String>, String) -> Unit,
    onAppearLastItem: () -> Unit,
    onPostCreateButtonClick: () -> Unit,
    onMoreVertClick: (String) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        if (isLoadingPosts) {
            MaxSizeLoadingIndicator()
        } else {
            PostItemList(
                listState = listState,
                uiStates = uiState.posts,
                onUserIconClick = { onUserIconClick(it) },
                onContentClick = { onContentClick(it) },
                onImageClick = { imageUrls, clickedUrl -> onImageClick(imageUrls, clickedUrl) },
                onAppearLastItem = { onAppearLastItem() },
                onMoreVertClick = onMoreVertClick,
            )
        }
        RoundIconActionButton(
            icon = Icons.Filled.Add,
            onClick = onPostCreateButtonClick,
            modifier = Modifier.padding(paddingLarge)
        )
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun HomeUIPreview() = PreviewContainer {
    HomeUI(
        uiState = HomeScreenUiState.Default,
        listState = rememberLazyListState(),
        isLoadingPosts = false,
        onRefresh = {},
        onUserIconClick = {},
        onContentClick = {},
        onImageClick = { _, _ -> },
        onAppearLastItem = {},
        onPostCreateButtonClick = {},
        onMoreVertClick = {},
    )
}
