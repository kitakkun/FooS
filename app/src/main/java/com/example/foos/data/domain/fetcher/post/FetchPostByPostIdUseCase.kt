package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

/**
 * 投稿IDからPostをフェッチするユースケース
 */
class FetchPostByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(postId: String) : Post? {
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