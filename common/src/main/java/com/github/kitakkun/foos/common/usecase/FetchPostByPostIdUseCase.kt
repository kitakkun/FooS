package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository

/**
 * 投稿IDからPostをフェッチするユースケース
 */
class FetchPostByPostIdUseCase(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(postId: String): Post? {
        val dbPost = postsRepository.fetchByPostId(postId)
        val dbReactions = reactionsRepository.fetchReactionsByPostId(postId = postId)
        dbPost?.let {
            val dbUser = usersRepository.fetchByUserId(it.userId)
            dbUser?.let {
                return Post(post = dbPost, user = dbUser, reaction = dbReactions)
            }
        }
        return null
    }
}
