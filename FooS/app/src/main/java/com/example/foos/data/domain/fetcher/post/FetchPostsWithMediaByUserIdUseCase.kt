package com.example.foos.data.domain.fetcher.post

import com.example.foos.data.model.Post
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.model.database.DatabaseReaction
import com.example.foos.data.model.database.DatabaseUser
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

class FetchPostsWithMediaByUserIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
    private val reactionsRepository: ReactionsRepository,
) {

    suspend operator fun invoke(userId: String, start: Date? = null, end: Date? = null): List<Post> {
        val jobs = mutableListOf<Job>()
        var dbUser: DatabaseUser? = null
        var dbPosts: List<DatabasePost> = listOf()
        val reactions = mutableMapOf<String, List<DatabaseReaction>>()

        coroutineScope {
            jobs.add(async { dbUser = usersRepository.fetchByUserId(userId) })
            jobs.add(async { dbPosts = postsRepository.fetchWithMediaByUserId(userId, start, end, PostsRepositoryImpl.DEFAULT_LOAD_LIMIT) })
        }
        jobs.joinAll()
        jobs.clear()

        coroutineScope {
            dbPosts.forEach {
                reactions[it.postId] = reactionsRepository.fetchReactionsByPostId(it.postId)
            }
        }
        jobs.joinAll()

        dbUser?.let {
            return dbPosts.map {
                Post(post = it, user = dbUser!!, reaction = reactions[it.postId] ?: listOf())
            }
        }
        return listOf()
    }
}