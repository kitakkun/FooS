package com.example.foos.data.repository

import com.example.foos.data.model.DatabaseFollow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * フォロー状態を管理するリポジトリ
 */
object FollowRepository {

    private const val COLLECTION = "follows"

    /**
     * 指定ユーザのフォロワーを情報を取得します
     */
    suspend fun fetchFollowers(userId: String): List<DatabaseFollow> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("followee", userId)
            .get().await().toObjects(DatabaseFollow::class.java).toList()
    }

    /**
     * 指定ユーザがフォローしているユーザを取得します
     */
    suspend fun fetchFollowees(userId: String): List<DatabaseFollow> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("follower", userId)
            .get().await().toObjects(DatabaseFollow::class.java).toList()
    }

    /**
     * フォロー関係を作成
     */
    suspend fun create(follower: String, followee: String) {
        if (followee == Firebase.auth.uid) { return }
        val entry = DatabaseFollow(follower = follower, followee = followee)
        val document = Firebase.firestore.collection(COLLECTION).document()
        document.set(entry).await()
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * フォロー関係を削除
     */
    suspend fun delete(follower: String, followee: String) {
        val documents = Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("follower", follower)
            .whereEqualTo("followee", followee)
            .get().await().documents
        documents.forEach {
            it.reference.delete()
        }
    }
}