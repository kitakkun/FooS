package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.foos.ui.navigation.Screen
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.view.component.MaxSizeLoadingIndicator
import com.example.foos.ui.view.component.button.RoundIconActionButton
import com.example.foos.ui.view.component.list.PostItemList
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
    LaunchedEffect(uiState.posts.isEmpty()) {
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
        if (uiState.posts.isEmpty()) {
            MaxSizeLoadingIndicator()
        } else {
            PostItemList(
                listState = listState,
                uiStates = uiState.posts,
                onUserIconClick = { viewModel.onUserIconClick(it) },
                onContentClick = { viewModel.onContentClick(it) },
                onImageClick = { imageUrls, clickedImageUrl ->
                    viewModel.onImageClick(
                        imageUrls,
                        clickedImageUrl
                    )
                },
                onAppearLastItem = { viewModel.fetchOlderPosts() }
            )
        }
    }
    RoundIconActionButton(onClick = { navController.navigate(SubScreen.PostCreate.route) })
}
