package com.example.foos.ui.state.screen.home

/**
 * ホーム画面のUI状態
 * @param posts 投稿のUI状態のリスト
 * @param isRefreshing 新規投稿読み込み中かどうか
 */
data class HomeScreenUiState(
    val posts: List<PostItemUiState>,
    val isRefreshing: Boolean,
)