package com.github.kitakkun.foos.user.followlist

import com.github.kitakkun.foos.user.com.github.kitakkun.foos.user.followlist.FollowUserUiState

data class FollowListScreenUiState(
    val followers: List<FollowUserUiState> = emptyList(),
    val followingUsers: List<FollowUserUiState> = emptyList(),
    val shouldShowFollowingListFirst: Boolean = true,
    val isFollowerListRefreshing: Boolean = false,
    val isFollowingListRefreshing: Boolean = false,
) {
    companion object {
        fun buildTestData() = FollowListScreenUiState(
//            followers = List(6) { FollowerUserUiState.buildTestData() },
//            followingUsers = List(4) { UserItemUiState.buildTestData() }
        )
    }
}
