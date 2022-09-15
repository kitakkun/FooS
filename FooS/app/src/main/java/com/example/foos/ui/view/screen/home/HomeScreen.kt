package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.foos.ui.navigation.Screen
import com.example.foos.ui.state.screen.home.HomeScreenUiState
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.MaxSizeLoadingIndicator
import com.example.foos.ui.view.component.button.RoundIconActionButton
import com.example.foos.ui.view.component.list.PostItemList
import com.example.foos.ui.view.screen.ScreenViewModel
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
        // 初回起動時の投稿フェッチ
        launch {
            viewModel.fetchNewerPosts()
        }
        // ナビゲーションイベント処理
        launch {
            viewModel.navEvent.collect {
                navController.navigate(it)
            }
        }
        launch {
            // Bottomナビゲーションでホームがクリックされたらトップへスクロール
            screenViewModel.navRoute.collect {
                if (it == Screen.Home.route) {
                    listState.animateScrollToItem(0, 0)
                }
            }
        }
    }

    HomeUI(
        uiState = uiState, listState = listState,
        onPostCreateButtonClick = { viewModel.onPostCreateButtonClick() },
        onAppearLastItem = { viewModel.fetchOlderPosts() },
        onImageClick = { imageUrls, clickedUrl -> viewModel.onImageClick(imageUrls, clickedUrl) },
        onUserIconClick = { viewModel.onUserIconClick(it) },
        onRefresh = { viewModel.fetchNewerPosts() },
        onContentClick = { viewModel.onContentClick(it) }
    )

}

@Composable
private fun HomeUI(
    uiState: HomeScreenUiState,
    listState: LazyListState,
    onRefresh: () -> Unit,
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit,
    onImageClick: (List<String>, String) -> Unit,
    onAppearLastItem: () -> Unit,
    onPostCreateButtonClick: () -> Unit
) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
        onRefresh = onRefresh
    ) {
        if (uiState.posts.isEmpty()) {
            MaxSizeLoadingIndicator()
        } else {
            PostItemList(
                listState = listState,
                uiStates = uiState.posts,
                onUserIconClick = { onUserIconClick(it) },
                onContentClick = { onContentClick(it) },
                onImageClick = { imageUrls, clickedUrl -> onImageClick(imageUrls, clickedUrl) },
                onAppearLastItem = { onAppearLastItem() }
            )
        }
    }
    RoundIconActionButton(icon = Icons.Filled.Add, onClick = onPostCreateButtonClick)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeUIPreview() {
    FooSTheme {
        HomeUI(
            uiState = HomeScreenUiState.Default,
            listState = rememberLazyListState(),
            onRefresh = {},
            onUserIconClick = {},
            onContentClick = {},
            onImageClick = { _, _ -> },
            onAppearLastItem = {},
            onPostCreateButtonClick = {}
        )
    }
}
