package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.ext.join
import com.github.kitakkun.foos.common.model.DatabaseReaction
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

/**
 * リアクションデータの読み書きを行うためのリポジトリ
 */
class ReactionsRepositoryImpl(
    private val database: FirebaseFirestore,
) : ReactionsRepository {

    companion object {
        private const val COLLECTION = "reactions"
    }

    override suspend fun fetchByPostIds(postids: List<String>): List<DatabaseReaction> =
        if (postids.isEmpty()) listOf()
        else if (postids.size > 10) {
            postids.chunked(10).map { fetchByPostIds(it) }.join()
        } else {
            database.collection(COLLECTION)
                .whereIn("postId", postids)
                .get().await().toObjects(DatabaseReaction::class.java)
        }

    override suspend fun fetchByUserIdWithDate(
        userId: String,
        start: Date?,
        end: Date?,
    ): List<DatabaseReaction> {
        var query = database.collection(COLLECTION).whereEqualTo("from", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
        start?.let { query = query.whereGreaterThanOrEqualTo("createdAt", start) }
        end?.let { query = query.whereLessThanOrEqualTo("createdAt", end) }
        return query.get().await().toObjects(DatabaseReaction::class.java).toList()
    }

    /**
     * 投稿に対応するリアクションデータを取得します
     * @param postId 投稿ID
     * @return 指定した投稿に紐づくリアクションのリスト
     */
    override suspend fun fetchReactionsByPostId(postId: String): List<DatabaseReaction> {
        return database.collection(COLLECTION)
            .whereEqualTo("postId", postId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(DatabaseReaction::class.java).toList()
    }

    /**
     * 特定ユーザに関連するリアクションデータを取得します
     * @param userId ユーザID
     * @param received 受け取ったものを選ぶかどうか
     * @return 指定したユーザに紐づくリアクションのリスト
     */
    override suspend fun fetchReactionsByUserId(
        userId: String,
        received: Boolean
    ): List<DatabaseReaction> {
        val field = if (received) "to" else "from"
        return database.collection(COLLECTION)
            .whereEqualTo(field, userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(DatabaseReaction::class.java).toList()
    }

    /**
     * リアクションを作成します
     * @param databaseReaction 登録するリアクションデータ
     */
    override suspend fun create(databaseReaction: DatabaseReaction) {
        val document = database.collection(COLLECTION).document()
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
    override suspend fun delete(reactionId: String) {
        database.collection(COLLECTION).document(reactionId).delete()
    }
}
