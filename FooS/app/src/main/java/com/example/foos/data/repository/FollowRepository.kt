package com.example.foos.data.repository

import com.example.foos.data.model.MyFollowingState
import com.example.foos.data.model.database.DatabaseFollow

interface FollowRepository {

    suspend fun fetchFollowState(from: String, to: String): MyFollowingState

    suspend fun fetchFollowers(followeeId: String): List<DatabaseFollow>

    suspend fun fetchFollowees(followerId: String): List<DatabaseFollow>

    suspend fun create(follower: String, followee: String)

    suspend fun delete(follower: String, followee: String)
}