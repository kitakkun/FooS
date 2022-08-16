package com.example.foos.ui.home

import androidx.compose.runtime.Stable

data class HomeScreenUiState(
    val posts: List<PostItemUiState>,
    val isRefreshing: Boolean,
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