package com.example.foos.data.domain

import com.example.foos.data.model.DatabasePost
import com.example.foos.data.model.DatabaseUser
import com.example.foos.data.model.Reaction
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.*

/**
 * 該当のユーザに関連するリアクションを取得
 */
class GetReactionsByUserIdUseCase constructor(
    private val usersRepository: UsersRepository,
    private val postsRepository: PostsRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String) : List<Reaction> {
        val postJobs = mutableListOf<Job>()
        val userJobs = mutableListOf<Job>()
        val reactions = reactionsRepository.fetchReactionsByUserId(userId, true)
        val posts = mutableMapOf<String, DatabasePost?>()
        val users = mutableMapOf<String, DatabaseUser?>()
        coroutineScope {
            reactions.forEach {
                postJobs.add (async { posts.put(it.reactionId, postsRepository.fetchByPostId(it.postId)) })
                userJobs.add (async { users.put(it.reactionId, usersRepository.fetchUser(it.from)) })
            }
        }
        postJobs.joinAll()
        userJobs.joinAll()
        return reactions.map {
            val reactionId = it.reactionId
            val post = posts.get(reactionId)
            val user = users.get(reactionId)
            if (post == null || user == null) {
                null
            } else {
                Reaction(reaction = it, post = post, user = user)
            }
        }.filterNotNull()
    }
}