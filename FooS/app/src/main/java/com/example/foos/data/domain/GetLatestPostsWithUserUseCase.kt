package com.example.foos.data.domain

import com.example.foos.data.model.PostWithUser
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

class GetLatestPostsWithUserUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(): List<PostWithUser> {
        val postsWithUsers = postsRepository.fetchNewerPosts().map { post ->
            val user = usersRepository.fetchUser(post.userId)
            user?.let {
                PostWithUser(it, post)
            }
        }
        return postsWithUsers.filterNotNull()
    }

}