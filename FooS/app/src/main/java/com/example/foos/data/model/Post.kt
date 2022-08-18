package com.example.foos.data.model

import java.util.*

data class Post (
    val postId: String,                 // 投稿ID
    val userId: String,                 // ユーザーID
    val content: String,                // 本文
    val attachedImages: List<String>,   // 画像のURL
    val longitude: Double?,             // 緯度
    val latitude: Double?,              // 経度
    val createdAt: Date,           // 投稿日時
) {
    constructor(): this("","","", listOf(), null, null, Date())
}
