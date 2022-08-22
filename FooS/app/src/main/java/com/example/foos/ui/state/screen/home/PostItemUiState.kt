package com.example.foos.ui.state.screen.home

/**
 * 投稿内容のUI状態
 * @param postId 投稿ID
 * @param userId ユーザID
 * @param username ユーザ名
 * @param userIcon ユーザアイコン
 * @param content 投稿の本文
 * @param attachedImages 添付画像のリスト
 */
data class PostItemUiState(
    val postId: String,
    val userId: String,
    val username: String,
    val userIcon: String,
    val content: String,
    val attachedImages: List<String>,
) {
    companion object {
        val Default = PostItemUiState(
            "postId",
            "userId",
            "username",
            "",
            "content",
            listOf("test"),
        )
    }
}