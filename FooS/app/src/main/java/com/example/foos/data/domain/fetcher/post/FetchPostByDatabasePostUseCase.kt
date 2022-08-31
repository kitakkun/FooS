package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import javax.inject.Inject

/**
 * DatabasePost型のデータから、Post型のデータをフェッチするユースケース
 */
class FetchPostByDatabasePostUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(dbPost: DatabasePost): Post? {
        val jobs = mutableListOf<Job>()
        var user: DatabaseUser? = null
        var reactions: List<DatabaseReaction> = listOf()

        coroutineScope {
            jobs.add(async {
                user = usersRepository.fetchByUserId(dbPost.userId)
            })
            jobs.add(async {
                reactions = reactionsRepository.fetchReactionsByPostId(dbPost.postId)
            })
        }

        jobs.joinAll()

        if (user != null) {
            return Post(post = dbPost, user = user!!, reaction = reactions)
        }

        return null
    }
}