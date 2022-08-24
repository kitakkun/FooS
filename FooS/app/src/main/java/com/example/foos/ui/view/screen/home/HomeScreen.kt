package com.example.foos.ui.view.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.foos.ui.view.component.RoundIconActionButton
import com.example.foos.ui.view.screen.Page
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * ホーム画面のコンポーザブル。ユーザーの投稿をリストで表示。
 * @param viewModel スクリーンに対応するViewModel
 * @param navController 画面遷移用のNavController
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = uiState.value.isRefreshing),
        onRefresh = { viewModel.fetchNewPosts() }
    ) {
        PostItemList(
            uiStates = uiState.value.posts,
            onUserIconClick = { userId -> viewModel.onUserIconClick(userId) },
            onContentClick = { uiState -> viewModel.onContentClick(uiState) },
            onImageClick = { uiState, clickedImageUrl ->
                viewModel.onImageClick(
                    uiState,
                    clickedImageUrl
                )
            }
        )
    }
    RoundIconActionButton(onClick = { navController.navigate(Page.PostCreate.route) })
}
