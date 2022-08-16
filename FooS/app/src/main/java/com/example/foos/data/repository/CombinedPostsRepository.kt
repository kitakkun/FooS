package com.example.foos.data.repository

import com.example.foos.data.repository.model.PostData
import javax.inject.Inject

class CombinedPostsRepository @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend fun fetchPosts(): List<PostData> {
        val combinedPosts = postsRepository.fetchNewerPosts().map { post ->
            val user = usersRepository.fetchUser(post.userId)
            user?.let {
                PostData(it, post)
            }
        }
        return combinedPosts.filterNotNull()
    }



}