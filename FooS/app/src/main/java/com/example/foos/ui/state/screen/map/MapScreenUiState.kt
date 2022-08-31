package com.example.foos.ui.state.screen.map

import com.example.foos.data.model.DatabasePost
import com.example.foos.ui.state.screen.home.PostItemUiState

data class MapScreenUiState(
    val posts: List<PostItemUiState>,
    val focusingPost: PostItemUiState,
)