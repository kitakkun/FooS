package com.example.foos.data.model

/**
 * ユーザーのデータベース上のデータ
 * @param userId ユーザID
 * @param username ユーザ名
 * @param profileImage プロフィール画像のURL
 */
data class User(
    val userId: String,
    val username: String,
    val profileImage: String,
) {
    constructor() : this("", "", "")
}