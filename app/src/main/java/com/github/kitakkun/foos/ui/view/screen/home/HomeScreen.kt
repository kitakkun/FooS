package com.github.kitakkun.foos.ui.view.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.ui.PreviewContainer
import com.github.kitakkun.foos.ui.constants.paddingLarge
import com.github.kitakkun.foos.ui.navigation.MainScreen
import com.github.kitakkun.foos.ui.state.screen.home.HomeScreenUiState
import com.github.kitakkun.foos.ui.view.component.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.ui.view.component.button.RoundIconActionButton
import com.github.kitakkun.foos.ui.view.component.list.PostItemList
import com.github.kitakkun.foos.ui.view.screen.ScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

/**
 * ホーム画面のコンポーザブル。ユーザーの投稿をリストで表示。
 * @param viewModel スクリーンに対応するViewModel
 * @param navController 画面遷移用のNavController
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    screenViewModel: ScreenViewModel
) {

    val uiState = viewModel.uiState.value
    val listState = rememberLazyListState()

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
            // Bottomナビゲーションでホームがクリックされたらトップへスクロール
            screenViewModel.navRoute.collect {
                if (it == MainScreen.Home.route) {
                    listState.animateScrollToItem(0, 0)
                }
            }
        }
    }

    HomeUI(
        uiState = uiState, listState = listState,
        onPostCreateButtonClick = { viewModel.onPostCreateButtonClick() },
        isLoadingPosts = false, /* TODO: ロードインディケータの適切な制御 */
        onAppearLastItem = { viewModel.fetchOlderPosts() },
        onImageClick = { imageUrls, clickedUrl -> viewModel.onImageClick(imageUrls, clickedUrl) },
        onUserIconClick = { viewModel.onUserIconClick(it) },
        onRefresh = { viewModel.onRefresh() },
        onContentClick = { viewModel.onContentClick(it) },
        onMoreVertClick = viewModel::onMoreVertClick,
    )

}

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
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
        onRefresh = onRefresh
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
        }
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
