package com.example.foos.data.domain

import com.example.foos.data.model.PostWithUser
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.UsersRepository
import java.util.*
import javax.inject.Inject

class GetPostsWithUserUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(from: Date = Date()): List<PostWithUser> {
        val postsWithUsers = postsRepository.fetchNewerPosts(from).map { post ->
            val user = usersRepository.fetchByUserId(post.userId)
            user?.let {
                PostWithUser(it, post)
            }
        }
        return postsWithUsers.filterNotNull()
    }

}