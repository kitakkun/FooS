package com.example.foos.data.repository

import com.example.foos.data.model.User
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
    suspend fun fetchUser(userId: String): User? {
        val userData = Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .get().await().toObjects(User::class.java)
        return if (userData.size > 0) {
            userData[0]
        } else {
            null
        }
    }

    /**
     * ユーザ情報を更新します
     */
    suspend fun update(user: User) {
        val document = Firebase.firestore.document(user.userId)
        val updates = hashMapOf<String, Any>(
            "username" to user.username,
            "profileImage" to user.profileImage
        )
        document.update(updates).await()
    }

    /**
     * ユーザを削除します
     */
    suspend fun delete(userId: String) {
        Firebase.firestore.document(userId).delete()
    }
}