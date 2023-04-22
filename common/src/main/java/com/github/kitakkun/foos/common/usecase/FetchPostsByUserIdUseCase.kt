package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import java.util.*

/**
 * ユーザIDを元に投稿をフェッチするユースケース
 */
class FetchPostsByUserIdUseCase(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String, end: Date = Date()): List<Post> {
        val dbUser = usersRepository.fetchByUserId(userId)
        val dbPosts = postsRepository.fetchByUserId(
            userId,
            null,
            end,
            PostsRepositoryImpl.DEFAULT_LOAD_LIMIT
        )
        val dbReactions = dbPosts.map { it.postId }.let { reactionsRepository.fetchByPostIds(it) }

        return dbPosts.mapNotNull { post ->
            val reactions = dbReactions.filter { it.postId == post.postId }
            dbUser?.let {
                Post(
                    post = post,
                    user = dbUser,
                    reaction = reactions,
                )
            }
        }
    }
}
