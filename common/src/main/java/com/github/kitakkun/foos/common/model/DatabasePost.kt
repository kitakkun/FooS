package com.github.kitakkun.foos.common.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * データベース上での投稿データ表現
 * @param postId 投稿のID
 * @param userId 投稿したユーザのID
 * @param content 投稿のテキスト本文
 * @param attachedImages 添付画像のURL
 * @param longitude 経度
 * @param latitude 緯度
 * @param createdAt 投稿日時（サーバーのタイムスタンプ）
 */
data class DatabasePost(
    val postId: String,
    val userId: String,
    val content: String,
    val attachedImages: List<String>,
    val longitude: Double?,
    val latitude: Double?,
    val locationName: String?,
    @ServerTimestamp
    val createdAt: Date? = null,
) {
    constructor() : this("", "", "", listOf(), null, null, null)
}
