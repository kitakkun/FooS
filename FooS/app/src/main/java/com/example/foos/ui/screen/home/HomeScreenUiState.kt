package com.example.foos.ui.screen.home

data class HomeScreenUiState(
    val posts: List<PostItemUiState>,
    val isRefreshing: Boolean,
)

data class PostItemUiState(
    val postId: String,
    val userId: String,
    val username: String,
    val userIcon: String,
    val content: String,
    val attachedImages: List<String>,
) {
    companion object {
        val Default = PostItemUiState(
            "postId",
            "userId",
            "username",
            "",
            "content",
            listOf("test"),
        )
    }
}