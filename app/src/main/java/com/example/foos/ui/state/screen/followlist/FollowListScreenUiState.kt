package com.example.foos.ui.state.screen.followlist

data class FollowListScreenUiState(
    val followers: List<UserItemUiState>,
    val followees: List<UserItemUiState>,
)
