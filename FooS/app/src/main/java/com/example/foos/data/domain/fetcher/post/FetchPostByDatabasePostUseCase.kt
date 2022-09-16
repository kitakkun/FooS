package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

/**
 * DatabasePost型のデータから、Post型のデータをフェッチするユースケース
 */
class FetchPostByDatabasePostUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(dbPost: DatabasePost): Post? {
        val dbUser = usersRepository.fetchByUserId(dbPost.userId)
        val dbReactions = reactionsRepository.fetchReactionsByPostId(dbPost.postId)

        dbUser?.let {
            return Post(post = dbPost, user = it, reaction = dbReactions)
        }

        return null
    }
}