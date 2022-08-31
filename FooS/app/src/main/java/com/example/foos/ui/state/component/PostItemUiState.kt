package com.example.foos.ui.state.component

import android.os.Parcelable
import com.example.foos.data.model.DatabaseReaction
import kotlinx.parcelize.Parcelize
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
    val createdAt: Date? = null,
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