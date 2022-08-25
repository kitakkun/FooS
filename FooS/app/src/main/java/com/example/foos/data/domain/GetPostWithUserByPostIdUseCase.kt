package com.example.foos.data.domain

import com.example.foos.data.model.PostWithUser
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

class GetPostWithUserByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(postId: String): PostWithUser? {
        val post = postsRepository.fetchByPostId(postId)
        post?.let {
            val user = usersRepository.fetchByUserId(post.userId)
            user?.let {
                return PostWithUser(it, post)
            }
        }
        return null
    }

}