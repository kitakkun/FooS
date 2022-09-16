package com.example.foos.ui.state.screen.userprofile

import com.example.foos.ui.state.component.PostItemUiState

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
    val mediaPosts: List<PostItemUiState>,
    val userReactedPosts: List<PostItemUiState>,
    val followerCount: Int,
    val followeeCount: Int,
    val following: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingPosts: Boolean = false,
    val isLoadingMediaPosts: Boolean = false,
    val isLoadingUserReactedPosts: Boolean = false,
) {
    companion object {
        val Default = UserProfileScreenUiState(
            userId = "userId",
            username = "username",
            userIcon = "",
            posts = listOf(),
            mediaPosts = listOf(),
            userReactedPosts = listOf(),
            followerCount = 0,
            followeeCount = 0,
        )
    }
}
