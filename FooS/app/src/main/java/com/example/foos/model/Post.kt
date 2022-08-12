package com.example.foos.model

data class Post(
    val username: String,
    val content: String,
    val attachedImages: List<String>,
)
