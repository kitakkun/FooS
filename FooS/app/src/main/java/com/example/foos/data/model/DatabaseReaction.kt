package com.example.foos.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * リアクションのデータ
 * @param reactionId リアクションID
 * @param postId 投稿ID
 * @param userId リアクションしたユーザID
 * @param reaction リアクションの絵文字
 * @param createdAt リアクションが作成された日時
 */
data class DatabaseReaction(
    val reactionId: String,
    val postId: String,
    val userId: String,
    val reaction: String,
    @ServerTimestamp
    val createdAt: Date? = null,
)
