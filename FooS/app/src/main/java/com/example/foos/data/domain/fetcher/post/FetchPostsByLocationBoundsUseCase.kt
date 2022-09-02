package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.PostsRepositoryImpl
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
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
        val jobs = mutableListOf<Job>()
        val dbPosts = postsRepository.fetchByLatLngBounds(bounds)
        val dbUsers = mutableMapOf<String, DatabaseUser?>()
        val dbReactions = mutableMapOf<String, List<DatabaseReaction>>()
        coroutineScope {
            dbPosts.forEach {
                // 投稿者の情報をフェッチ
                jobs.add(async { dbUsers.put(it.postId, usersRepository.fetchByUserId(it.userId)) })
                // 投稿に対するリアクションをフェッチ
                jobs.add(async {
                    dbReactions.put( it.postId, reactionsRepository.fetchReactionsByPostId(it.postId) )
                })
            }
        }
        jobs.joinAll()
        return dbPosts.mapNotNull { post ->
            dbUsers[post.postId]?.let { user ->
                Post(
                    post = post,
                    user = user,
                    reaction = dbReactions[post.postId] ?: listOf()
                )
            }
        }
    }
}