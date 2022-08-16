package com.example.foos.model

data class PostData(
    val userId: String,     // ユーザーID
    val content: String,        // 本文
    val attachedImages: List<String>,   // 画像のURL
)
