package com.github.kitakkun.foos.user

data class UserItemUiState(
    val isClientUser: Boolean = false,
    val username: String,
    val userId: String,
    val profileImage: String,
    val bio: String,
    val following: Boolean,
    val followingYou: Boolean,
) {
    companion object {
        val Default = UserItemUiState(
            isClientUser = false,
            username = "username",
            userId = "userId",
            profileImage = "",
            bio = "",
            following = false,
            followingYou = false,
        )
    }
}
