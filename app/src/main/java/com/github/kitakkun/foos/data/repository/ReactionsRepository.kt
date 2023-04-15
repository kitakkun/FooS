package com.github.kitakkun.foos.data.repository

import com.github.kitakkun.foos.common.model.DatabaseReaction
import java.util.*

interface ReactionsRepository {

    suspend fun fetchByPostIds(postids: List<String>): List<DatabaseReaction>
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
