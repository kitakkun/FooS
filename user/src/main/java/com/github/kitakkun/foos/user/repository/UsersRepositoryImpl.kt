package com.github.kitakkun.foos.user.repository

import android.util.Log
import com.github.kitakkun.foos.user.join
import com.github.kitakkun.foos.user.model.DatabaseUser
import com.github.kitakkun.foos.user.model.Email
import com.github.kitakkun.foos.user.model.Password
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * ユーザ情報を管理するリポジトリ
 */
class UsersRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : UsersRepository {

    companion object {
        private const val COLLECTION = "users"
        private const val TAG = "UsersRepository"
    }

    override suspend fun fetchByUserIds(userIds: List<String>): List<DatabaseUser> =
        if (userIds.isEmpty()) listOf()
        else if (userIds.size > 10)
            userIds.chunked(10).map { fetchByUserIds(it) }.join()
        else database.collection(COLLECTION)
            .whereIn("userId", userIds.toSet().toList())
            .get().await().toObjects(DatabaseUser::class.java)

    override suspend fun create(email: Email, password: Password): Result<DatabaseUser, Throwable> {
        try {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(email.value, password.value).await()
            val user = result.user ?: return Err(Throwable("failed to create user."))
            val databaseUser = DatabaseUser(
                userId = user.uid,
                username = user.displayName ?: "user",
                profileImage = "",
            )
            database.collection(COLLECTION)
                .document(databaseUser.userId)
                .set(databaseUser).await()
            Log.d(TAG, "Successfully created user.")
            return Ok(databaseUser)
        } catch (e: Throwable) {
            Log.d(TAG, "Failed to create user.")
            return Err(e)
        }
    }

    override suspend fun create(databaseUser: DatabaseUser) {
        database.collection(COLLECTION)
            .document(databaseUser.userId)
            .set(databaseUser).await()
    }

    /**
     * ユーザ情報を取得します
     */
    override suspend fun fetchByUserId(userId: String): DatabaseUser? {
        val databaseUserData = database.collection(COLLECTION)
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
    override suspend fun update(databaseUser: DatabaseUser) {
        val document = database.collection(COLLECTION)
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
    override suspend fun delete(userId: String) {
        database.collection(COLLECTION).document(userId).delete()
    }
}
