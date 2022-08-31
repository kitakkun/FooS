package com.example.foos.ui.state.screen.map

import com.example.foos.ui.state.component.PostItemUiState

data class MapScreenUiState(
    val posts: List<PostItemUiState>,
    val focusingPost: PostItemUiState,
)