package com.example.foos.ui.state.screen.post

data class PostScreenUiState(
    var content: String,
    val attachedImages: List<String>,
)