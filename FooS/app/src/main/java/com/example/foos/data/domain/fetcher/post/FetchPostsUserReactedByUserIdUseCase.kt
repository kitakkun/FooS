package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.repository.ReactionsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
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
        val jobs = mutableListOf<Job>()
        val posts = mutableMapOf<String, Post>()
        val reactions = reactionsRepository.fetchByUserIdWithDate(userId, start, end)
        coroutineScope {
            reactions.forEach { reaction ->
                jobs.add(async {
                    fetchPostByPostIdUseCase(reaction.postId)?.let {
                        posts[reaction.reactionId] = it
                    }
                })
            }
        }
        jobs.joinAll()
        return reactions.mapNotNull { reaction -> posts[reaction.reactionId] }
    }
}