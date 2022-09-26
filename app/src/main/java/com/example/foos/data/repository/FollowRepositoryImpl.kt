package com.example.foos.data.repository

import com.example.foos.data.model.database.DatabaseFollow
import com.example.foos.util.join
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * フォロー状態を管理するリポジトリ
 */
class FollowRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) : FollowRepository {

    companion object {
        private const val COLLECTION = "follows"
    }

    override suspend fun fetch(followee: String, follower: String): DatabaseFollow? {
        database.collection(COLLECTION)
            .whereEqualTo("followee", followee)
            .whereEqualTo("follower", follower)
            .limit(1)
            .get().await().toObjects(DatabaseFollow::class.java).apply {
                return if (size > 0) get(0) else null
            }
    }

    /**
     * followeeのユーザIDでフォローデータをフィルタしてフェッチ
     */
    override suspend fun fetchByFolloweeId(followeeId: String): List<DatabaseFollow> =
        database.collection(COLLECTION)
            .whereEqualTo("followee", followeeId)
            .get().await().toObjects(DatabaseFollow::class.java)

    override suspend fun fetchByFollowerId(followerId: String): List<DatabaseFollow> =
        database.collection(COLLECTION)
            .whereEqualTo("follower", followerId)
            .get().await().toObjects(DatabaseFollow::class.java)

    /**
     * 各フォロイーのユーザIDをfolloweeに含むデータベースエントリをフェッチ
     */
    override suspend fun fetchByFolloweeIds(followeeIds: List<String>): List<DatabaseFollow> =
        if (followeeIds.isEmpty()) listOf()
        else if (followeeIds.size > 10)
            followeeIds.chunked(10).map { fetchByFolloweeIds(it) }.join()
        else database.collection(COLLECTION)
            .whereIn("followee", followeeIds)
            .get().await().toObjects(DatabaseFollow::class.java)

    /**
     * 各フォロワーのユーザIDをfollowerに含むデータベースエントリをフェッチ
     */
    override suspend fun fetchByFollowerIds(followerIds: List<String>): List<DatabaseFollow> =
        if (followerIds.size > 10) {
            followerIds.chunked(10).map { fetchByFolloweeIds(it) }.join()
        } else {
            database.collection(COLLECTION)
                .whereIn("follower", followerIds)
                .get().await().toObjects(DatabaseFollow::class.java)
        }

    /**
     * フォロー関係を作成
     */
    override suspend fun create(follower: String, followee: String) {
        if (followee == auth.uid) {
            return
        }
        val entry = DatabaseFollow(follower = follower, followee = followee)
        val document = database.collection(COLLECTION).document()
        document.set(entry).await()
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * フォロー関係を削除
     */
    override suspend fun delete(follower: String, followee: String) {
        val documents = database.collection(COLLECTION)
            .whereEqualTo("follower", follower)
            .whereEqualTo("followee", followee)
            .get().await().documents
        documents.forEach {
            it.reference.delete()
        }
    }
}