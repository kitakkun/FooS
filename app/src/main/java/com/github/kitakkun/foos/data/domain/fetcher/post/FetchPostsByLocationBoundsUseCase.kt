package com.github.kitakkun.foos.data.domain.fetcher.post

import com.github.kitakkun.foos.common.model.Post
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.user.repository.UsersRepository
import com.google.android.gms.maps.model.LatLngBounds
import javax.inject.Inject

/**
 * 位置情報の範囲から投稿をフェッチするユースケース
 */
class FetchPostsByLocationBoundsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(bounds: LatLngBounds): List<Post> {
        val dbPosts = postsRepository.fetchByLatLngBounds(bounds)
        val dbUsers = usersRepository.fetchByUserIds(dbPosts.map { it.userId })
        val dbReactions = reactionsRepository.fetchByPostIds(dbPosts.map { it.postId })
        return dbPosts.mapNotNull { post ->
            val user = dbUsers.find { it.userId == post.userId }
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
