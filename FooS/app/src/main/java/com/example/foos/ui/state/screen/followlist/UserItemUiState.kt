package com.example.foos.ui.state.screen.followlist

data class UserItemUiState(
    val clientUserId: String,
    val username: String,
    val userId: String,
    val profileImage: String,
    val bio: String,
    val following: Boolean,
    val followingYou: Boolean,
)
