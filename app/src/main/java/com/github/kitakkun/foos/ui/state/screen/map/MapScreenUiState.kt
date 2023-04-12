package com.github.kitakkun.foos.ui.state.screen.map

import com.github.kitakkun.foos.ui.state.component.PostItemUiState

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
