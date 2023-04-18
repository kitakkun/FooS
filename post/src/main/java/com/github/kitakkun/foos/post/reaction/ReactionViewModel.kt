package com.github.kitakkun.foos.post.reaction

import kotlinx.coroutines.flow.StateFlow

interface ReactionViewModel {
    val uiState: StateFlow<ReactionScreenUiState>
    fun fetchNewReactions(indicateRefreshing: Boolean = false)
    fun onUserIconClick(userId: String)
    fun onContentClick(reactionId: String)
}
