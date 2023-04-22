package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.FollowGraph
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * フォロー状態を管理するリポジトリ
 */
class FollowRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) : FollowRepository {

    companion object {
        private const val COLLECTION = "follows"
    }

    override suspend fun createFollowGraph(from: String, to: String) {
        if (from != auth.uid) return
        if (to == auth.uid) return
        database.collection(COLLECTION).document().apply {
            val entry = FollowGraph(from = from, to = to)
            set(entry).await()
            update("createdAt", FieldValue.serverTimestamp()).await()
        }
    }

    override suspend fun deleteFollowGraph(from: String, to: String) {
        database.collection(COLLECTION)
            .whereEqualTo("from", from)
            .whereEqualTo("to", to)
            .get().await().forEach {
                it.reference.delete()
            }
    }

    override suspend fun fetchFollowerUserIds(userId: String): List<String> =
        database.collection(COLLECTION)
            .whereEqualTo("to", userId)
            .get().await()
            .toObjects(FollowGraph::class.java)
            .mapNotNull { it.from }

    override suspend fun fetchFollowingUserIds(userId: String): List<String> =
        database.collection(COLLECTION)
            .whereEqualTo("from", userId)
            .get().await()
            .toObjects(FollowGraph::class.java)
            .mapNotNull { it.from }

    override suspend fun fetch(from: String, to: String): FollowGraph? =
        database.collection(COLLECTION)
            .whereEqualTo("to", to)
            .whereEqualTo("from", from)
            .limit(1)
            .get().await().toObjects(FollowGraph::class.java)
            .firstOrNull()

    override suspend fun fetchFollowerCount(userId: String): Int =
        database.collection(COLLECTION)
            .whereEqualTo("to", userId)
            .get().await().size()

    override suspend fun fetchFollowingCount(userId: String): Int =
        database.collection(COLLECTION)
            .whereEqualTo("from", userId)
            .get().await().size()

    @Deprecated("Use fetchFollowerUserIds instead")
    override suspend fun fetchByFolloweeId(followeeId: String): List<FollowGraph> =
        database.collection(COLLECTION)
            .whereEqualTo("to", followeeId)
            .get().await().toObjects(FollowGraph::class.java)

    @Deprecated("Use fetchFollowingUserIds instead")
    override suspend fun fetchByFollowerId(followerId: String): List<FollowGraph> =
        database.collection(COLLECTION)
            .whereEqualTo("from", followerId)
            .get().await().toObjects(FollowGraph::class.java)
}
