package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import java.util.*

/**
 * フィルタなしで投稿をフェッチするユースケース
 */
class FetchPostsUseCase(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {
    suspend operator fun invoke(start: Date? = null, end: Date? = null): List<Post> {
        val dbPosts = postsRepository.fetch(start, end, PostsRepositoryImpl.DEFAULT_LOAD_LIMIT)
        val dbUsers = usersRepository.fetchByUserIds(dbPosts.map { it.userId })
        val dbReactions = reactionsRepository.fetchByPostIds(dbPosts.map { it.postId })

        return dbPosts.mapNotNull { post ->
            val user = dbUsers.find { it.id == post.userId } ?: return@mapNotNull null
            val reactions = dbReactions.filter { it.postId == post.postId }
            Post(
                post = post,
                user = user,
                reaction = reactions,
            )
        }
    }
}
