package com.example.foos.ui.state.screen.reaction

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
            username = "username",
            userIcon = "",
            reaction = "😭",
            postContent = "post content..."
        )
    }
}