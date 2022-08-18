package com.example.foos.ui.reaction

data class ReactionScreenUiState(
    val reactions: List<ReactionItemUiState>
)

data class ReactionItemUiState(
    val username: String,       // リアクションを行ったユーザ名
    val userIcon: String,       // ユーザーアイコンのURL
    val reaction: String,       // リアクション内容
    val postContent: String,    // リアクションされた投稿の内容
) {
    companion object {
        val Default = ReactionItemUiState("username", "https://cdn-icons-png.flaticon.com/512/1006/1006771.png", "❤️", "post content...")
    }
}