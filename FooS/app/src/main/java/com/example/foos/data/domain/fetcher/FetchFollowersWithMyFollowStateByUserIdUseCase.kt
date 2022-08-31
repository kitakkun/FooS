package com.example.foos.data.domain.fetcher

import com.example.foos.data.model.DatabaseUser
import com.example.foos.data.model.MyFollowingState
import com.example.foos.data.model.UserWithFollowState
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import javax.inject.Inject

/**
 * フォロー状態とユーザ情報をまとめたデータを取得する
 */
class FetchFollowersWithMyFollowStateByUserIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
) {

    /**
     * userIdのフォロワーをクライアントのフォロー状態と一緒に取得
     */
    suspend operator fun invoke(myUserId: String, userId: String): List<UserWithFollowState> {
        val followers = followRepository.fetchFollowers(userId).map { it.follower } // フォロー情報
        val jobs = mutableListOf<Job>()
        val users = mutableListOf<DatabaseUser>()
        val followStates = mutableMapOf<String, MyFollowingState>()
        coroutineScope {
            // 各フォロワーに関して
            followers.forEach {
                // ユーザー情報を取得
                jobs.add(async { usersRepository.fetchByUserId(it)?.let { users.add(it) } })
                // クライアントと該当ユーザとのフォロー関係を解決
                jobs.add(async {
                    followStates.put(
                        it,
                        followRepository.fetchFollowState(from = myUserId, to = it)
                    )
                })
            }
        }
        jobs.joinAll()
        return users.mapNotNull { user ->
            val followState = followStates[user.userId]
            if (followState != null) {
                UserWithFollowState(
                    user = user,
                    followState = followState
                )
            } else {
                null
            }
        }
    }
}