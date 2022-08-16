package com.example.foos.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CombinedPostsRepository @Inject constructor(
    val postsRepository: PostsRepository,
    val usersRepository: UsersRepository,
) {

    suspend fun fetchPosts() {
        postsRepository.fetchInitialPosts()
    }

}