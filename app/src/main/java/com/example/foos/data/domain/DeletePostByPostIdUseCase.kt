package com.example.foos.data.domain

import com.example.foos.data.repository.PostsRepository
import javax.inject.Inject

class DeletePostByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository
){
    suspend operator fun invoke(postId: String) {
        postsRepository.deletePost(postId = postId)
    }
}