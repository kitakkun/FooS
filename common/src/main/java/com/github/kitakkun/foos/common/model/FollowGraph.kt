package com.github.kitakkun.foos.common.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class FollowGraph(
    val from: String,
    val to: String,
    @ServerTimestamp
    val createdAt: Date? = null,
) {
    constructor() : this(
        from = "", to = ""
    )
}
