package com.github.kitakkun.foos.user.com.github.kitakkun.foos.user.followlist

data class FollowUserUiState(
    val id: String = "",
    val name: String = "",
    val profileImageUrl: String? = null,
    val biography: String = "",
    val isFollowButtonVisible: Boolean = false,
    val isFollowsYouVisible: Boolean = false,
    val isFollowedByClientUser: Boolean = false,
)
