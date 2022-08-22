package com.example.foos.ui.view.screen.post

data class PostScreenUiState(
    var content: String,
    val attachedImages: List<String>,
    var posting: Boolean,
)