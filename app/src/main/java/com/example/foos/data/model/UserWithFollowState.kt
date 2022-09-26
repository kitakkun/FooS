package com.example.foos.data.model

import com.example.foos.data.model.database.DatabaseUser

data class UserWithFollowState(
    val user: DatabaseUser,
    val followState: FollowState,
)
