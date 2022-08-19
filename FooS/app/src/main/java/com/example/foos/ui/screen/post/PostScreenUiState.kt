package com.example.foos.ui.screen.post

data class PostScreenUiState(
    var content: String,
    val attachedImages: List<String>,
    var posting: Boolean,
)