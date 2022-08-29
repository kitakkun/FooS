package com.example.foos.data.model

data class UserWithFollowState(
    val user: DatabaseUser,
    val followState: MyFollowingState,
)
