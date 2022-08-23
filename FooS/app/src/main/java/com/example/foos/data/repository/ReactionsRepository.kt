package com.example.foos.data.repository

import com.example.foos.data.model.Reaction
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * リアクションデータの読み書きを行うためのリポジトリ
 */
object ReactionsRepository {

    /**
     * 投稿に対応するリアクションデータを取得します
     * @param postId 投稿ID
     * @return 指定した投稿に紐づくリアクションのリスト
     */
    suspend fun fetchReactionsByPostId(postId: String): List<Reaction> {
        return Firebase.firestore.collection(FirestoreDao.COLLECTION_REACTIONS)
            .whereArrayContains("postId", postId)
            .get().await().toObjects(Reaction::class.java).toList()
    }

    /**
     * 特定ユーザが行ったリアクションデータを取得します
     * @param userId ユーザID
     * @return 指定したユーザに紐づくリアクションのリスト
     */
    suspend fun fetchReactionsByUserId(userId: String): List<Reaction> {
        return Firebase.firestore.collection(FirestoreDao.COLLECTION_REACTIONS)
            .whereArrayContains("userId", userId)
            .get().await().toObjects(Reaction::class.java).toList()
    }

    /**
     * リアクションを作成します
     * @param reaction 登録するリアクションデータ
     */
    suspend fun create(reaction: Reaction) {
        val docRef = FirestoreDao.createDocumentReference(FirestoreDao.COLLECTION_REACTIONS)
        docRef.set(reaction).await()
        val updateData = hashMapOf<String, Any>(
            "createdAt" to FieldValue.serverTimestamp(),
            "reactionId" to docRef.id
        )
        docRef.update(updateData).await()
    }

    /**
     * リアクションを削除します
     * @param reactionId 削除するリアクションのID
     */
    suspend fun delete(reactionId: String) {
        FirestoreDao.delete(FirestoreDao.COLLECTION_REACTIONS, reactionId)
    }
}