package com.example.foos

import com.example.foos.ui.state.screen.home.PostItemUiState

object Posts {
    fun getPosts(): List<PostItemUiState> {
        return listOf(
            PostItemUiState(
                "postId",
                "userid",
                "username10",
                "id",
                "content",
                listOf()
            ),
        )
    }
}