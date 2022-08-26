package com.example.foos.data.domain

import com.example.foos.data.model.PostWithUser
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.*
import javax.inject.Inject

/**
 * 投稿内容とユーザデータの結合型PostWithUserのリストを、
 * ユーザIDとフィルタ用日付を指定して取得するためのユースケース
 */
class GetPostsWithUserByUserIdWithDateUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(userId: String, start: Date? = null, end: Date? = null) : List<PostWithUser> {
        val jobs = mutableListOf<Job>()
        val user = usersRepository.fetchByUserId(userId = userId) ?: return listOf()
        val posts = postsRepository.fetchByUserIdWithDate(userId = userId, start = start, end = end)
        return posts.map { PostWithUser(user, it) }
    }
}