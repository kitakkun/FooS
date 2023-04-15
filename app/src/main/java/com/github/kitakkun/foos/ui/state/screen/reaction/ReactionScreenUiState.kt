package com.github.kitakkun.foos.ui.state.screen.reaction

import com.github.kitakkun.foos.common.model.Reaction

data class ReactionScreenUiState(
    val reactions: List<ReactionItemUiState>,
    val isRefreshing: Boolean,
)

data class ReactionItemUiState(
    val username: String,       // リアクションを行ったユーザ名
    val userIcon: String,       // ユーザーアイコンのURL
    val reaction: String,       // リアクション内容
    val postContent: String,    // リアクションされた投稿の内容
) {
    companion object {
        val Default = ReactionItemUiState(
            username = "",
            userIcon = "",
            reaction = "",
            postContent = ""
        )

        fun convert(reaction: Reaction): ReactionItemUiState =
            ReactionItemUiState(
                username = reaction.user.username,
                userIcon = reaction.user.profileImage,
                postContent = reaction.post.content,
                reaction = reaction.reaction.reaction
            )
    }
}
