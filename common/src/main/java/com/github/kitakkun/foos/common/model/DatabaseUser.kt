package com.github.kitakkun.foos.common.model

/**
 * ユーザーのデータベース上のデータ
 * @param id ユーザID
 * @param name ユーザ名
 * @param profileImage プロフィール画像のURL
 */
data class DatabaseUser(
    val id: String,
    val name: String,
    val profileImage: String,
) {
    constructor() : this(
        id = "", name = "", profileImage = ""
    )
}
