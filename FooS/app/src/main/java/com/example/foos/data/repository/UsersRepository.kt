package com.example.foos.data.repository

import com.example.foos.data.model.database.DatabaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * ユーザ情報を管理するリポジトリ
 */
object UsersRepository {

    private const val COLLECTION = "users"

    /**
     * ユーザ情報を取得します
     */
    suspend fun fetchByUserId(userId: String): DatabaseUser? {
        val databaseUserData = Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .get().await().toObjects(DatabaseUser::class.java)
        return if (databaseUserData.size > 0) {
            databaseUserData[0]
        } else {
            null
        }
    }

    /**
     * ユーザ情報を更新します
     */
    suspend fun update(databaseUser: DatabaseUser) {
        val document = Firebase.firestore.collection(COLLECTION)
            .document(databaseUser.userId)
        val updates = hashMapOf<String, Any>(
            "username" to databaseUser.username,
            "profileImage" to databaseUser.profileImage
        )
        document.update(updates).await()
    }

    /**
     * ユーザを削除します
     */
    suspend fun delete(userId: String) {
        Firebase.firestore.collection(COLLECTION).document(userId).delete()
    }
}