package com.github.kitakkun.foos.ui.screen.map

import com.github.kitakkun.foos.customview.composable.post.PostItemUiState

data class MapScreenUiState(
    val posts: List<PostItemUiState>,
    val focusingPost: PostItemUiState,
) {
    companion object {
        val Default = MapScreenUiState(
            posts = listOf(),
            focusingPost = PostItemUiState.Default
        )
    }
}
