package com.github.kitakkun.foos.data.model.database

/**
 * ユーザーのデータベース上のデータ
 * @param userId ユーザID
 * @param username ユーザ名
 * @param profileImage プロフィール画像のURL
 */
data class DatabaseUser(
    val userId: String,
    val username: String,
    val profileImage: String,
) {
    constructor() : this(
        userId = "", username = "", profileImage = ""
    )
}
