package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.follow.FollowGraph
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

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

    override suspend fun fetchFollowGraph(from: String, to: String): FollowGraph? =
        database.collection(COLLECTION)
            .whereEqualTo("to", to)
            .whereEqualTo("from", from)
            .limit(1)
            .get().await().toObjects(FollowGraph::class.java)
            .firstOrNull()

    override suspend fun fetchFollowerGraphs(
        userId: String,
        newerThan: Date?,
        olderThan: Date?,
    ): List<FollowGraph> =
        database.collection(COLLECTION).apply {
            if (newerThan != null) whereGreaterThan("createdAt", newerThan)
            if (olderThan != null) whereLessThan("createdAt", olderThan)
            whereEqualTo("to", userId)
        }.get().await().toObjects(FollowGraph::class.java)

    override suspend fun fetchFollowingGraphs(
        userId: String,
        newerThan: Date?,
        olderThan: Date?,
    ): List<FollowGraph> =
        database.collection(COLLECTION).apply {
            if (newerThan != null) whereGreaterThan("createdAt", newerThan)
            if (olderThan != null) whereLessThan("createdAt", olderThan)
            whereEqualTo("from", userId)
        }.get().await().toObjects(FollowGraph::class.java)

    override suspend fun fetchFollowerCount(userId: String): Long {
        return database.collection(COLLECTION)
            .whereEqualTo("to", userId)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count
    }

    override suspend fun fetchFollowingCount(userId: String): Long {
        return database.collection(COLLECTION)
            .whereEqualTo("from", userId)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count
    }
}
