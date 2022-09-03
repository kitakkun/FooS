package com.example.foos.data.repository

import com.example.foos.data.model.database.DatabaseFollow
import com.example.foos.data.model.MyFollowingState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.tasks.await

/**
 * フォロー状態を管理するリポジトリ
 */
class FollowRepositoryImpl : FollowRepository {

    companion object {
        private const val COLLECTION = "follows"
    }

    override suspend fun fetchFollowState(from: String, to: String): MyFollowingState {
        var following: Boolean = false
        var followed: Boolean = false
        val jobs = mutableListOf<Job>()
        coroutineScope {
            jobs.add(async { followed = isFollowed(from, to) })
            jobs.add(async { following = isFollowing(from, to) })
        }
        jobs.joinAll()
        return MyFollowingState(following = following, followed = followed)
    }

    private suspend fun isFollowing(from: String, to: String): Boolean {
        return !Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("followee", to)
            .whereEqualTo("follower", from)
            .get().await().isEmpty
    }

    private suspend fun isFollowed(from: String, to: String): Boolean {
        return !Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("follower", to)
            .whereEqualTo("followee", from)
            .get().await().isEmpty
    }

    /**
     * 指定ユーザのフォロワーを情報を取得します
     */
    override suspend fun fetchFollowers(followeeId: String): List<DatabaseFollow> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("followee", followeeId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(DatabaseFollow::class.java).toList()
    }

    /**
     * 指定ユーザがフォローしているユーザを取得します
     */
    override suspend fun fetchFollowees(followerId: String): List<DatabaseFollow> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("follower", followerId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await().toObjects(DatabaseFollow::class.java).toList()
    }

    /**
     * フォロー関係を作成
     */
    override suspend fun create(follower: String, followee: String) {
        if (followee == Firebase.auth.uid) {
            return
        }
        val entry = DatabaseFollow(follower = follower, followee = followee)
        val document = Firebase.firestore.collection(COLLECTION).document()
        document.set(entry).await()
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * フォロー関係を削除
     */
    override suspend fun delete(follower: String, followee: String) {
        val documents = Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("follower", follower)
            .whereEqualTo("followee", followee)
            .get().await().documents
        documents.forEach {
            it.reference.delete()
        }
    }
}