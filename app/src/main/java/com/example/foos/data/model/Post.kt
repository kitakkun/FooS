package com.example.foos.data.model

import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser

/**
 * 投稿一つのデータ
 */
data class Post(
    val post: DatabasePost,
    val user: DatabaseUser,
    val reaction: List<DatabaseReaction>,
)
