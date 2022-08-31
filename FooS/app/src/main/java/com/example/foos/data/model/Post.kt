package com.example.foos.data.model

/**
 * 投稿一つのデータ
 */
data class Post(
    val post: DatabasePost,
    val user: DatabaseUser,
    val reaction: List<DatabaseReaction>,
)
