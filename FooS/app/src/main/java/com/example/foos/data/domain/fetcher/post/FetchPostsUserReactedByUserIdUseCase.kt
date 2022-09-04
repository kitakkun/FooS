package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.repository.ReactionsRepository
import java.util.*
import javax.inject.Inject

/**
 * 特定のユーザがリアクションした投稿をまとめて取得するユースケース
 */
class FetchPostsUserReactedByUserIdUseCase @Inject constructor(
    private val reactionsRepository: ReactionsRepository,
    private val fetchPostByPostIdUseCase: FetchPostByPostIdUseCase,
) {

    suspend operator fun invoke(
        userId: String,
        start: Date? = null,
        end: Date? = null
    ): List<Post> {
        val reactions = reactionsRepository.fetchByUserIdWithDate(userId, start, end)
        val userReactedPostIds = reactions.map { it.postId }
        return userReactedPostIds.mapNotNull { fetchPostByPostIdUseCase(it) }
    }
}