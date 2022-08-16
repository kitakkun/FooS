package com.example.foos.data.repository.model

data class UserData(
    val userId: String,         // ユーザーID
    val username: String,       // ユーザー名
    val profileImage: String,   // プロフィール画像のURL
) {
    constructor(): this("", "", "")
}