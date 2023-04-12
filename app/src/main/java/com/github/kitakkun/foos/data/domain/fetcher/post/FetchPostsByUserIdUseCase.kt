package com.github.kitakkun.foos.data.domain.fetcher.post

import com.github.kitakkun.foos.data.model.Post
import com.github.kitakkun.foos.data.repository.PostsRepository
import com.github.kitakkun.foos.data.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.data.repository.ReactionsRepository
import com.github.kitakkun.foos.user.repository.UsersRepository
import java.util.*
import javax.inject.Inject

/**
 * ユーザIDを元に投稿をフェッチするユースケース
 */
class FetchPostsByUserIdUseCase @Inject constructor(
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
