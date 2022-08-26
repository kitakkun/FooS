package com.example.foos.data.repository

import com.example.foos.data.model.DatabaseReaction
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * リアクションデータの読み書きを行うためのリポジトリ
 */
object ReactionsRepository {

    private const val COLLECTION = "reactions"

    /**
     * 投稿に対応するリアクションデータを取得します
     * @param postId 投稿ID
     * @return 指定した投稿に紐づくリアクションのリスト
     */
    suspend fun fetchReactionsByPostId(postId: String): List<DatabaseReaction> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("postId", postId)
            .get().await().toObjects(DatabaseReaction::class.java).toList()
    }

    /**
     * 特定ユーザに関連するリアクションデータを取得します
     * @param userId ユーザID
     * @param received 受け取ったものを選ぶかどうか
     * @return 指定したユーザに紐づくリアクションのリスト
     */
    suspend fun fetchReactionsByUserId(
        userId: String,
        received: Boolean = true
    ): List<DatabaseReaction> {
        val field = if (received) "to" else "from"
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo(field, userId)
            .get().await().toObjects(DatabaseReaction::class.java).toList()
    }

    /**
     * リアクションを作成します
     * @param databaseReaction 登録するリアクションデータ
     */
    suspend fun create(databaseReaction: DatabaseReaction) {
        val document = Firebase.firestore.collection(COLLECTION).document()
        document.set(databaseReaction).await()
        val updateData = hashMapOf(
            "createdAt" to FieldValue.serverTimestamp(),
            "reactionId" to document.id
        )
        document.update(updateData).await()
    }

    /**
     * リアクションを削除します
     * @param reactionId 削除するリアクションのID
     */
    suspend fun delete(reactionId: String) {
        Firebase.firestore.collection(COLLECTION).document(reactionId).delete()
    }
}