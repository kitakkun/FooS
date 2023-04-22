package com.github.kitakkun.foos.user.followlist

import com.github.kitakkun.foos.user.composable.UserItemUiState

data class FollowListScreenUiState(
    val followers: List<UserItemUiState> = emptyList(),
    val followingUsers: List<UserItemUiState> = emptyList(),
    val shouldShowFollowingListFirst: Boolean = true,
) {
    companion object {
        fun buildTestData() = FollowListScreenUiState(
            followers = List(6) { UserItemUiState.buildTestData() },
            followingUsers = List(4) { UserItemUiState.buildTestData() }
        )
    }
}
