package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.foos.ui.view.component.RoundButtonRow
import com.example.foos.ui.view.component.RoundIconActionButton
import com.example.foos.ui.view.screen.Page
import com.example.foos.ui.view.screen.Screen
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

    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // 起動時初回フェッチ
    LaunchedEffect(Unit) {
        viewModel.fetchNewerPosts()
    }

    // ナビゲーションイベントの処理
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    // Bottomナビゲーションのイベントを受け取る
    LaunchedEffect(Unit) {
        screenViewModel.navRoute.collect {
            if (it == Screen.Home.route) {
                listState.animateScrollToItem(0, 0)
            }
        }
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = uiState.value.isRefreshing),
        onRefresh = { viewModel.onRefresh() }
    ) {
        PostItemList(
            header = {RoundButtonRow(titles = listOf("Nearby", "Following", "Trending"), defaultColor = Color.LightGray, selectedColor = Color.Green)},
            listState = listState,
            uiStates = uiState.value.posts,
            onUserIconClick = { userId -> viewModel.onUserIconClick(userId) },
            onContentClick = { uiState -> viewModel.onContentClick(uiState) },
            onImageClick = { uiState, clickedImageUrl ->
                viewModel.onImageClick(
                    uiState,
                    clickedImageUrl
                )
            },
            onAppearLastItem = { viewModel.fetchOlderPosts() }
        )
    }
    RoundIconActionButton(onClick = { navController.navigate(Page.PostCreate.route) })
}
