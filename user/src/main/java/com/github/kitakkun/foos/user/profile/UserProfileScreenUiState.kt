package com.github.kitakkun.foos.user.profile

import com.github.kitakkun.foos.customview.composable.post.PostItemUiState

data class UserProfileScreenUiState(
    val id: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val posts: List<PostItemUiState> = emptyList(),
    val mediaPosts: List<PostItemUiState> = emptyList(),
    val reactedPosts: List<PostItemUiState> = emptyList(),
    val followerCount: Int = 0,
    val followCount: Int = 0,
    val isFollowedByClientUser: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingPosts: Boolean = false,
    val isLoadingMediaPosts: Boolean = false,
    val isLoadingUserReactedPosts: Boolean = false,
)
