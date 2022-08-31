package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import javax.inject.Inject

/**
 * 投稿IDからPostをフェッチするユースケース
 */
class FetchPostByPostIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(postId: String) : Post? {
        val jobs = mutableListOf<Job>()
        var dbPost: DatabasePost? = null
        var dbUser: DatabaseUser? = null
        var dbReactions: List<DatabaseReaction> = listOf()
        coroutineScope {
            // 投稿内容のフェッチ
            jobs.add(async { dbPost = postsRepository.fetchByPostId(postId = postId) })
            // リアクションのフェッチ
            jobs.add(async { dbReactions = reactionsRepository.fetchReactionsByPostId(postId = postId) })
        }
        jobs.joinAll()
        dbPost?.let {
            dbUser = usersRepository.fetchByUserId(it.userId)
        }
        return if (dbPost != null && dbUser != null) {
            Post(post = dbPost!!, user = dbUser!!, reaction = dbReactions)
        } else {
            null
        }
    }
}