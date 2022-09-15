package com.example.foos.ui.state.component

import com.example.foos.data.model.Post
import com.example.foos.data.model.database.DatabaseReaction
import java.util.*

/**
 * 投稿内容のUI状態
 * @param postId 投稿ID
 * @param userId ユーザID
 * @param username ユーザ名
 * @param userIcon ユーザアイコン
 * @param content 投稿の本文
 * @param attachedImages 添付画像のリスト
 * @param latitude 緯度
 * @param longitude 経度
 * @param createdAt 投稿日時
 */
data class PostItemUiState(
    val postId: String,
    val userId: String,
    val username: String,
    val userIcon: String,
    val content: String,
    val attachedImages: List<String> = listOf(),
    val reactions: List<DatabaseReaction> = listOf(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null,
    val createdAt: Date? = null,
) {

    companion object {
        val Default = PostItemUiState(
            postId = "",
            userId = "",
            username = "",
            userIcon = "",
            content = "",
            attachedImages = listOf(),
        )

        fun convert(post: Post) : PostItemUiState {
            return PostItemUiState(
                postId = post.post.postId,
                userIcon = post.user.profileImage,
                username = post.user.username,
                longitude = post.post.longitude,
                latitude = post.post.latitude,
                createdAt = post.post.createdAt,
                content = post.post.content,
                attachedImages = post.post.attachedImages,
                userId = post.user.userId,
                reactions = post.reaction,
            )
        }
    }

}
