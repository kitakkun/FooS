package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.google.android.gms.maps.model.LatLngBounds

/**
 * 位置情報の範囲から投稿をフェッチするユースケース
 */
class FetchPostsByLocationBoundsUseCase(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(bounds: LatLngBounds): List<Post> {
        val dbPosts = postsRepository.fetchByLatLngBounds(bounds)
        val dbUsers = usersRepository.fetchByUserIds(dbPosts.map { it.userId })
        val dbReactions = reactionsRepository.fetchByPostIds(dbPosts.map { it.postId })
        return dbPosts.mapNotNull { post ->
            val user = dbUsers.find { it.id == post.userId }
            val reactions = dbReactions.filter { it.postId == post.postId }
            user?.let {
                Post(
                    post = post,
                    user = user,
                    reaction = reactions,
                )
            }
        }
    }
}
