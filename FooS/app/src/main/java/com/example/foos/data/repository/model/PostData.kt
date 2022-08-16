package com.example.foos.data.repository.model

data class PostData(
    val postId: String,                 // 投稿ID
    val userId: String,                 // ユーザーID
    val content: String,                // 本文
    val attachedImages: List<String>,   // 画像のURL
    val longitude: Double?,             // 緯度
    val latitude: Double?,              // 経度
) {
    constructor(): this("","","", listOf(), null, null)
}
