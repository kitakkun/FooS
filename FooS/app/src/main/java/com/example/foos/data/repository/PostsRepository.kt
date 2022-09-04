package com.example.foos.data.repository

import android.content.Context
import com.example.foos.data.model.database.DatabasePost
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

interface PostsRepository {

    suspend fun fetch(start: Date?, end: Date?, count: Long): List<DatabasePost>

    suspend fun fetchWithMediaByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long
    ): List<DatabasePost>

    suspend fun fetchByLatLngBounds(bounds: LatLngBounds): List<DatabasePost>

    suspend fun fetchByUserIdWithDate(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost>

    suspend fun fetchByUserIdsWithDate(
        userIds: List<String>,
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost>

    suspend fun fetchByUserId(userId: String, count: Long): List<DatabasePost>

    suspend fun fetchByUserIds(userIds: List<String>, count: Long): List<DatabasePost>

    suspend fun fetchByPostId(postId: String): DatabasePost?

    suspend fun fetchByPostIds(postIds: List<String>): List<DatabasePost>

    suspend fun create(databasePost: DatabasePost, context: Context)

    suspend fun deletePost(postId: String)

    suspend fun fetchNewerPosts(from: Date): List<DatabasePost>

    suspend fun fetchOlderPosts(from: Date): List<DatabasePost>
}