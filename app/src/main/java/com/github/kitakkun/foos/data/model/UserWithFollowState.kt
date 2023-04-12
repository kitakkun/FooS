package com.github.kitakkun.foos.data.model

import com.github.kitakkun.foos.user.model.DatabaseUser

data class UserWithFollowState(
    val user: DatabaseUser,
    val followState: FollowState,
)
