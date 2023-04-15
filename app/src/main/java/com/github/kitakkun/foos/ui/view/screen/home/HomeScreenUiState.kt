package com.github.kitakkun.foos.ui.view.screen.home

import com.github.kitakkun.foos.customview.composable.post.PostItemUiState

/**
 * ホーム画面のUI状態
 * @param posts 投稿のUI状態のリスト
 * @param isRefreshing 新規投稿読み込み中かどうか
 */
data class HomeScreenUiState(
    val posts: List<PostItemUiState>,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {
    companion object {
        val Default = HomeScreenUiState(
            posts = listOf(),
            isRefreshing = false,
            isLoading = false,
        )
    }
}
