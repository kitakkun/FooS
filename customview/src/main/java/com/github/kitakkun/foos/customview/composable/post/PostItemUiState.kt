package com.github.kitakkun.foos.customview.composable.post

import com.github.kitakkun.foos.common.model.DatabaseReaction
import com.github.kitakkun.foos.common.model.Post
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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

    val formattedCreatedAtText
        get() :String =
            if (createdAt != null) {
                val createdAtDateTime =
                    createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                val nowDateTime = LocalDateTime.now()
                val daysDiff = ChronoUnit.DAYS.between(createdAtDateTime, nowDateTime)
                val hoursDiff = ChronoUnit.HOURS.between(createdAtDateTime, nowDateTime)
                val minutesDiff = ChronoUnit.MINUTES.between(createdAtDateTime, nowDateTime)
                val secondsDiff = ChronoUnit.SECONDS.between(createdAtDateTime, nowDateTime)
                if (daysDiff >= 7) {
                    // ex) 9 Jan
                    createdAtDateTime.format(DateTimeFormatter.ofPattern("d MMM"))
                } else if (daysDiff >= 1) {
                    // ex) 5d
                    "${daysDiff}d"
                } else if (hoursDiff >= 1) {
                    // ex) 7h
                    "${hoursDiff}h"
                } else if (minutesDiff >= 1) {
                    // ex) 10m
                    "${minutesDiff}m"
                } else {
                    // ex) 40s
                    "${secondsDiff}s"
                }
            } else {
                ""
            }

    companion object {
        val Default = PostItemUiState(
            postId = "",
            userId = "",
            username = "",
            userIcon = "",
            content = "",
            attachedImages = listOf(),
        )

        fun convert(post: Post): PostItemUiState {
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
