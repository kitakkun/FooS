package com.example.foos.ui.view.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.ui.state.screen.home.PostItemUiState
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

    viewModel.setNavController(navController)

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

/**
 * 投稿のリスト
 * @param uiStates 各投稿のUI状態
 * @param onUserIconClick ユーザアイコンクリック時の挙動
 * @param onContentClick コンテンツクリック時の挙動
 * @param onImageClick 添付画像クリック時の挙動
 */
@Composable
fun PostItemList(
    uiStates: List<PostItemUiState>,
    onUserIconClick: (userId: String) -> Unit = { },
    onContentClick: (uiState: PostItemUiState) -> Unit = { },
    onImageClick: (uiState: PostItemUiState, clickedImageUrl: String) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(uiStates) { post ->
            PostItem(post, onUserIconClick, onContentClick, onImageClick)
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}
