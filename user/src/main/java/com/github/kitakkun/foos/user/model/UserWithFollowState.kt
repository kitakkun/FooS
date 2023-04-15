package com.github.kitakkun.foos.user.model

import com.github.kitakkun.foos.common.model.DatabaseUser
import com.github.kitakkun.foos.common.model.FollowState

data class UserWithFollowState(
    val user: DatabaseUser,
    val followState: FollowState,
)
