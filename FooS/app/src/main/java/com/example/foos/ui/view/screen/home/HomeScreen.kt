package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.foos.ui.view.component.RoundIconActionButton
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.navigation.Screen
import com.example.foos.ui.view.screen.ScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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

    // 初回起動時の投稿フェッチ
    LaunchedEffect(Unit) {
        viewModel.fetchNewerPosts()
    }

    // ナビゲーションイベント処理
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    // Bottomナビゲーションでホームがクリックされたらトップへスクロール
    LaunchedEffect(Unit) {
        screenViewModel.navRoute.collect {
            if (it == Screen.Home.route) {
                listState.animateScrollToItem(0, 0)
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
        onRefresh = { viewModel.onRefresh() }
    ) {
        PostItemList(
            listState = listState,
            uiStates = uiState.posts,
            onUserIconClick = { viewModel.onUserIconClick(it) },
            onContentClick = { viewModel.onContentClick(it) },
            onImageClick = { uiState, clickedImageUrl ->
                viewModel.onImageClick(
                    uiState,
                    clickedImageUrl
                )
            },
            onAppearLastItem = { viewModel.fetchOlderPosts() }
        )
    }
    RoundIconActionButton(onClick = { navController.navigate(SubScreen.PostCreate.route) })
}
