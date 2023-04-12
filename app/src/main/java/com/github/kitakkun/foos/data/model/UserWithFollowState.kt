package com.github.kitakkun.foos.data.model

import com.github.kitakkun.foos.data.model.database.DatabaseUser

data class UserWithFollowState(
    val user: DatabaseUser,
    val followState: FollowState,
)
