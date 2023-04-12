package com.github.kitakkun.foos.user.followlist

import com.github.kitakkun.foos.user.UserItemUiState

data class FollowListScreenUiState(
    val followers: List<UserItemUiState>,
    val followees: List<UserItemUiState>,
)
