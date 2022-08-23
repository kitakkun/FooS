package com.example.foos.data.domain

import com.example.foos.data.model.Reaction
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import javax.inject.Inject

/**
 * 該当のユーザに関連するリアクションを取得
 */
class GetReactionsByUserIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val postsRepository: PostsRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String) : List<Reaction> {
        val reactions = reactionsRepository.fetchReactionsByUserId(userId).map { reaction ->
            val post = postsRepository.fetchPost(reaction.postId)
            val user = usersRepository.fetchUser(reaction.userId)
            if (post != null && user != null) {
                Reaction(post = post, user = user, reaction = reaction)
            } else {
                null
            }
        }
        return reactions.filterNotNull()
    }
}