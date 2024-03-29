package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import java.util.*

/**
 * 特定のユーザがリアクションした投稿をまとめて取得するユースケース
 */
class FetchPostsUserReactedByUserIdUseCase(
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
