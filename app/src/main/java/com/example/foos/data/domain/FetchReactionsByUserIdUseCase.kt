package com.example.foos.data.domain

import com.example.foos.data.model.Reaction
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository

/**
 * 該当のユーザに関連するリアクションを取得
 */
class FetchReactionsByUserIdUseCase constructor(
    private val usersRepository: UsersRepository,
    private val postsRepository: PostsRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String): List<Reaction> {
        val reactions = reactionsRepository.fetchReactionsByUserId(userId, true)
        val posts = postsRepository.fetchByPostIds(reactions.map { it.postId })
        val users = usersRepository.fetchByUserIds(reactions.map { it.from })
        return reactions.mapNotNull { reaction ->
            val post = posts.find { it.postId == reaction.postId }
            val user = users.find { it.userId == reaction.from }
            if (post == null || user == null) {
                null
            } else {
                Reaction(reaction = reaction, post = post, user = user)
            }
        }
    }
}