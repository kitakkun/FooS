package com.example.foos.data.repository

import android.util.Log
import com.example.foos.data.repository.model.CombinedPostData
import javax.inject.Inject

class CombinedPostsRepository @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend fun fetchPosts(): List<CombinedPostData> {
        val combinedPosts = postsRepository.fetchNewerPosts().map { post ->
            val user = usersRepository.fetchUser(post.userId)
            Log.d("TAG", (user != null).toString())
            user?.let {
                CombinedPostData(it, post)
            }
        }
        return combinedPosts.filterNotNull()
    }

}