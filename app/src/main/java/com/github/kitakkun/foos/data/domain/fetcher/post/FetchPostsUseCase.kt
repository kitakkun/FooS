package com.github.kitakkun.foos.data.domain.fetcher.post

import com.github.kitakkun.foos.data.model.Post
import com.github.kitakkun.foos.data.repository.PostsRepository
import com.github.kitakkun.foos.data.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.data.repository.ReactionsRepository
import com.github.kitakkun.foos.user.repository.UsersRepository
import java.util.*
import javax.inject.Inject

/**
 * フィルタなしで投稿をフェッチするユースケース
 */
class FetchPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(start: Date? = null, end: Date? = null): List<Post> {
        val dbPosts = postsRepository.fetch(start, end, PostsRepositoryImpl.DEFAULT_LOAD_LIMIT)
        val dbUsers = usersRepository.fetchByUserIds(dbPosts.map { it.userId })
        val dbReactions = reactionsRepository.fetchByPostIds(dbPosts.map { it.postId })

        return dbPosts.mapNotNull { post ->
            val user = dbUsers.find { it.userId == post.userId }
            val reactions = dbReactions.filter { it.postId == post.postId }
            user?.let {
                Post(
                    post = post,
                    user = user,
                    reaction = reactions,
                )
            }
        }

    }

}