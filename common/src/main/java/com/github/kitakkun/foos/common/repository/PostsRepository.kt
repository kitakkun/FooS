package com.github.kitakkun.foos.common.repository

import android.content.Context
import com.github.kitakkun.foos.common.model.DatabasePost
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

interface PostsRepository {

    suspend fun fetch(start: Date?, end: Date?, count: Long?): List<DatabasePost>

    suspend fun fetchByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long?
    ): List<DatabasePost>

    suspend fun fetchByUserIds(
        userIds: List<String>,
        start: Date?,
        end: Date?,
        count: Long?
    ): List<DatabasePost>

    suspend fun fetchPostsWithMediaByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long?
    ): List<DatabasePost>

    suspend fun fetchByPostId(postId: String): DatabasePost?

    suspend fun fetchByLatLngBounds(bounds: LatLngBounds): List<DatabasePost>

    suspend fun fetchByPostIds(postIds: List<String>): List<DatabasePost>

    suspend fun create(databasePost: DatabasePost, context: Context)

    suspend fun deletePost(postId: String)

}
