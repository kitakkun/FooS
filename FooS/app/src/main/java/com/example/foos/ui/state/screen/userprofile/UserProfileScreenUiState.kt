package com.example.foos.ui.state.screen.userprofile

import com.example.foos.ui.state.screen.home.PostItemUiState

/**
 * ユーザプロフィール画面のUI状態
 * @param userId ユーザID
 * @param username ユーザ名
 * @param userIcon プロフィール画像
 * @param posts ユーザの投稿
 */
data class UserProfileScreenUiState(
    val userId: String,
    val username: String,
    val userIcon: String,
    val posts: List<PostItemUiState>,
) {
    companion object {
        val Default = UserProfileScreenUiState(
            "userId", "username", "", listOf()
        )
    }
}
