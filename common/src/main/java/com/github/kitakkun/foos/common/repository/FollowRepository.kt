package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.follow.FollowGraph
import java.util.Date

interface FollowRepository {
    suspend fun createFollowGraph(from: String, to: String)
    suspend fun deleteFollowGraph(from: String, to: String)

    suspend fun fetchFollowGraph(from: String, to: String): FollowGraph?
    suspend fun fetchFollowingGraphs(
        userId: String,
        newerThan: Date? = null,
        olderThan: Date? = null,
    ): List<FollowGraph>

    suspend fun fetchFollowerGraphs(
        userId: String,
        newerThan: Date? = null,
        olderThan: Date? = null,
    ): List<FollowGraph>

    suspend fun fetchFollowingCount(userId: String): Long

    suspend fun fetchFollowerCount(userId: String): Long
}
