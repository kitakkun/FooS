package com.example.foos.ui.home

data class HomeUiState(
    val posts: List<PostItemUiState>
)

data class PostItemUiState(
    val userId: String,
    val username: String,
    val userIcon: String,
    val content: String,
    val attachedImages: List<String>,
) {
    companion object {
        val Default = PostItemUiState(
            "userId",
            "username",
            "",
            "content",
            listOf("test"),
        )
    }
}