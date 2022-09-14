package com.example.foos.ui.state.screen.followlist

data class UserItemUiState(
    val clientUserId: String,
    val username: String,
    val userId: String,
    val profileImage: String,
    val bio: String,
    val following: Boolean,
    val followingYou: Boolean,
) {
    companion object {
        val Default = UserItemUiState(
            clientUserId = "xxxxxx",
            username = "username",
            userId = "userId",
            profileImage = "",
            bio = "",
            following = false,
            followingYou = false,
        )
    }
}
