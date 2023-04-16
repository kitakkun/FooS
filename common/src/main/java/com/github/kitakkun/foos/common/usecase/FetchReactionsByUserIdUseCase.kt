package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Reaction
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository

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
