package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
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

    suspend operator fun invoke(bounds: LatLngBounds) : List<Post> {
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