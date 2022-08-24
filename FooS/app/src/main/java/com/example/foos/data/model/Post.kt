package com.example.foos.data.model

data class Post(
    val post: DatabasePost,
    val user: DatabaseUser,
    val reaction: List<DatabaseReaction>,
)
