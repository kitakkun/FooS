package com.example.foos.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * リアクションのデータ
 * @param reactionId リアクションID
 * @param postId 投稿ID
 * @param from リアクションしたユーザID
 * @param to リアクションされたユーザID
 * @param reaction リアクションの絵文字
 * @param createdAt リアクションが作成された日時
 */
data class DatabaseReaction(
    val reactionId: String,
    val postId: String,
    val from: String,
    val to: String,
    val reaction: String,
    @ServerTimestamp
    val createdAt: Date? = null,
) {
    constructor() : this("", "", "", "", "", null)
}
