package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.di.Modules_ProvidePostsRepositoryFactory
import java.util.*
import javax.inject.Inject

/**
 * 複数のユーザIDでフィルタリングした投稿をフェッチするユースケース
 */
class FetchPostsByUserIdsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val fetchPostByDatabasePostUseCase: FetchPostByDatabasePostUseCase,
) {

    suspend operator fun invoke(
        userId: String,
        start: Date? = null,
        end: Date? = null
    ): List<Post> {
//        postsRepository.fetchByUserIdWithDate()
        return listOf()


    }
}