package com.example.foos.data.repository

import com.example.foos.data.model.database.DatabaseReaction
import java.util.*

interface ReactionsRepository {

    suspend fun fetchByUserIdWithDate(
        userId: String,
        start: Date? = null,
        end: Date? = null,
    ): List<DatabaseReaction>

    suspend fun fetchReactionsByPostId(postId: String): List<DatabaseReaction>

    suspend fun fetchReactionsByUserId(
        userId: String,
        received: Boolean = true
    ): List<DatabaseReaction>

    suspend fun create(databaseReaction: DatabaseReaction)

    suspend fun delete(reactionId: String)

}