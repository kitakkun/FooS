package com.github.kitakkun.foos.data.model

import com.github.kitakkun.foos.data.model.database.DatabasePost
import com.github.kitakkun.foos.data.model.database.DatabaseReaction
import com.github.kitakkun.foos.data.model.database.DatabaseUser

/**
 * 投稿一つのデータ
 */
data class Post(
    val post: DatabasePost,
    val user: DatabaseUser,
    val reaction: List<DatabaseReaction>,
)
