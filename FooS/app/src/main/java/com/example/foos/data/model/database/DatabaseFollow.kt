package com.example.foos.data.model.database

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * データベース上でフォロー関係を示すデータ
 * @param follower フォローした側のユーザID
 * @param followee フォローされた側のユーザID
 * @param createdAt フォローされた日時
 */
data class DatabaseFollow(
    val follower: String,
    val followee: String,
    @ServerTimestamp
    val createdAt: Date? = null,
) {
    constructor() : this(
        follower = "", followee = ""
    )
}
