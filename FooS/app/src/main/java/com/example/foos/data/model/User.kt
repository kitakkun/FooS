package com.example.foos.data.model

data class User(
    val userId: String,         // ユーザーID
    val username: String,       // ユーザー名
    val profileImage: String,   // プロフィール画像のURL
) {
    constructor(): this("", "", "")
}