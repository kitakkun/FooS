package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.DatabasePost
import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository

/**
 * DatabasePost型のデータから、Post型のデータをフェッチするユースケース
 */
class FetchPostByDatabasePostUseCase(
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
