package com.example.foos.data.domain

import com.example.foos.data.model.DatabasePost
import com.example.foos.data.model.Reaction
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

/**
 * 取得した投稿データから
 * 投稿者のユーザIDやリアクションデータを付随させた投稿データを取得するためのユースケース
 */
class GetFullDataPostByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(postId: String) {

    }

    suspend operator fun invoke(databasePost: DatabasePost) {
    }
}