package com.example.foos.data.domain

import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

class GetPostsByUserIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String): List<Post> {
        val user = usersRepository.fetchByUserId(userId)
        user?.let {
            val posts = postsRepository.fetchByUserId(userId).map {
//                val reactions = reactionsRepository.fetchReactionsByPostId(it.postId)
                Post(post = it, user = user, reaction = listOf())
            }
            return posts
        }
        return listOf()
    }
}