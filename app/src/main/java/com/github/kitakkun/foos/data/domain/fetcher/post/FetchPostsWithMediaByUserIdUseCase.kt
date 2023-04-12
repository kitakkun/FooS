package com.github.kitakkun.foos.data.domain.fetcher.post

import com.github.kitakkun.foos.data.model.Post
import com.github.kitakkun.foos.data.repository.PostsRepository
import com.github.kitakkun.foos.data.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.data.repository.ReactionsRepository
import com.github.kitakkun.foos.data.repository.UsersRepository
import java.util.*
import javax.inject.Inject

class FetchPostsWithMediaByUserIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(
        userId: String,
        start: Date? = null,
        end: Date? = null
    ): List<Post> {
        val dbUser = usersRepository.fetchByUserId(userId)
        val dbPosts = postsRepository.fetchPostsWithMediaByUserId(
            userId,
            start,
            end,
            PostsRepositoryImpl.DEFAULT_LOAD_LIMIT
        )
        val dbReactions = reactionsRepository.fetchByPostIds(dbPosts.map { it.postId })
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
