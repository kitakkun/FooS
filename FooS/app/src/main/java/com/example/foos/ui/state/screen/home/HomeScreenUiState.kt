package com.example.foos.ui.state.screen.home

import com.example.foos.ui.state.component.PostItemUiState

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
