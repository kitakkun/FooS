package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.PostsRepositoryImpl
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import java.util.*
import javax.inject.Inject

/**
 * フィルタなしで投稿をフェッチするユースケース
 */
class FetchPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(start: Date? = null, end: Date? = null): List<Post> {
        val jobs = mutableListOf<Job>()
        val dbPosts = postsRepository.fetch(start, end, PostsRepositoryImpl.DEFAULT_LOAD_LIMIT)
        val dbUsers = mutableMapOf<String, DatabaseUser?>()
        val dbReactions = mutableMapOf<String, List<DatabaseReaction>>()
        coroutineScope {
            dbPosts.forEach {
                // 投稿者の情報をフェッチ
                jobs.add(async { dbUsers.put(it.postId, usersRepository.fetchByUserId(it.userId)) })
                // 投稿に対するリアクションをフェッチ
                jobs.add(async {
                    dbReactions.put(
                        it.postId,
                        reactionsRepository.fetchReactionsByPostId(it.postId)
                    )
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