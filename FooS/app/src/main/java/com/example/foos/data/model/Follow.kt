package com.example.foos.data.model

/**
 * フォロー関係を示すデータ
 * @param follower フォローした側のユーザID
 * @param followed フォローされた側のユーザID
 * @param createdAt フォローされた日時
 */
data class Follow(
    val follower: String,
    val followed: String,
    val createdAt: String,
)
