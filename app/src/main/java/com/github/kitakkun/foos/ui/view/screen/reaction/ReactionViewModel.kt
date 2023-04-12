package com.github.kitakkun.foos.ui.view.screen.reaction

import androidx.compose.runtime.State
import com.github.kitakkun.foos.ui.state.screen.reaction.ReactionScreenUiState

interface ReactionViewModel {
    val uiState: State<ReactionScreenUiState>

    fun fetchNewReactions(indicateRefreshing: Boolean = false)
    fun onUserIconClick(userId: String)
    fun onContentClick(reactionId: String)
}
