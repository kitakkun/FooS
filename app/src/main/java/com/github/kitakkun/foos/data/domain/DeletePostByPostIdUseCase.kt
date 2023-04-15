package com.github.kitakkun.foos.data.domain

import com.github.kitakkun.foos.common.repository.PostsRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class DeletePostByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val firebaseAuth: FirebaseAuth,
) {
    suspend operator fun invoke(postId: String) {
        val userId = postsRepository.fetchByPostId(postId)?.userId ?: return
        if (userId == firebaseAuth.currentUser?.uid) {
            postsRepository.deletePost(postId = postId)
        }
    }
}
