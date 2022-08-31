package com.example.foos.data.domain

import com.example.foos.data.model.Reaction
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll

/**
 * 該当のユーザに関連するリアクションを取得
 */
class FetchReactionsByUserIdUseCase constructor(
    private val usersRepository: UsersRepository,
    private val postsRepository: PostsRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String): List<Reaction> {
        val jobs = mutableListOf<Job>()
        val reactions = reactionsRepository.fetchReactionsByUserId(userId, true)
        val posts = mutableMapOf<String, DatabasePost?>()
        val users = mutableMapOf<String, DatabaseUser?>()
        coroutineScope {
            reactions.forEach {
                jobs.add(async {
                    posts.put(
                        it.reactionId,
                        postsRepository.fetchByPostId(it.postId)
                    )
                })
                jobs.add(async {
                    users.put(
                        it.reactionId,
                        usersRepository.fetchByUserId(it.from)
                    )
                })
            }
        }
        jobs.joinAll()
        return reactions.mapNotNull {
            val reactionId = it.reactionId
            val post = posts.get(reactionId)
            val user = users.get(reactionId)
            if (post == null || user == null) {
                null
            } else {
                Reaction(reaction = it, post = post, user = user)
            }
        }
    }
}