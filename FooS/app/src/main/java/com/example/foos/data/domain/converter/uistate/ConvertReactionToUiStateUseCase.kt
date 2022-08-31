package com.example.foos.data.domain.converter.uistate

import com.example.foos.data.model.Reaction
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState

/**
 * ReactionをReactionItemUiStateへ変換
 */
class ConvertReactionToUiStateUseCase {

    operator fun invoke(reaction: Reaction): ReactionItemUiState =
        ReactionItemUiState(
            username = reaction.user.username,
            userIcon = reaction.user.profileImage,
            postContent = reaction.post.content,
            reaction = reaction.reaction.reaction
        )
}