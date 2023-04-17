package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.FollowGraph

interface FollowRepository {
    suspend fun createFollowGraph(from: String, to: String)
    suspend fun deleteFollowGraph(from: String, to: String)


    suspend fun fetch(from: String, to: String): FollowGraph?
    suspend fun fetchFollowerUserIds(userId: String): List<String>
    suspend fun fetchFollowingUserIds(userId: String): List<String>

    suspend fun fetchFollowerCount(userId: String): Int
    suspend fun fetchFollowingCount(userId: String): Int

    suspend fun fetchByFollowerId(followerId: String): List<FollowGraph>
    suspend fun fetchByFolloweeId(followeeId: String): List<FollowGraph>
}
