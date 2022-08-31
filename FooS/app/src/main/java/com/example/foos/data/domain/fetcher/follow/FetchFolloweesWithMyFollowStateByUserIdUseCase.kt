package com.example.foos.data.domain.fetcher.follow

import com.example.foos.data.model.database.DatabaseUser
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
class FetchFolloweesWithMyFollowStateByUserIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
) {

    /**
     * userIdのフォロイーをクライアントのフォロー状態と一緒に取得
     */
    suspend operator fun invoke(myUserId: String, userId: String): List<UserWithFollowState> {
        val followees = followRepository.fetchFollowees(userId).map { it.followee } // フォロー情報
        val jobs = mutableListOf<Job>()
        val users = mutableListOf<DatabaseUser>()
        val followStates = mutableMapOf<String, MyFollowingState>()

        coroutineScope {
            followees.forEach {
                jobs.add(async { usersRepository.fetchByUserId(it)?.let { users.add(it) } })
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
