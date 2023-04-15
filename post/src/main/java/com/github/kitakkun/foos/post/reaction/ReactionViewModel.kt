package com.github.kitakkun.foos.post.reaction

import androidx.compose.runtime.State

interface ReactionViewModel {
    val uiState: State<ReactionScreenUiState>

    fun fetchNewReactions(indicateRefreshing: Boolean = false)
    fun onUserIconClick(userId: String)
    fun onContentClick(reactionId: String)
}
