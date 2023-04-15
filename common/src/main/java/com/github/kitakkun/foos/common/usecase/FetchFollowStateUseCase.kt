package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.FollowState
import com.github.kitakkun.foos.common.repository.FollowRepository
import javax.inject.Inject

class FetchFollowStateUseCase @Inject constructor(
    private val followRepository: FollowRepository,
) {

    suspend operator fun invoke(from: String, to: String): FollowState {
        val followed = followRepository.fetch(followee = from, follower = to) != null
        val following = followRepository.fetch(followee = to, follower = from) != null
        return FollowState(
            selfId = from,
            otherId = to,
            following = following,
            followed = followed,
        )
    }

    suspend operator fun invoke(from: String, to: List<String>): List<FollowState> {
        // fromのユーザのフォロワー
        val followers = followRepository.fetchByFolloweeId(from).map { it.follower }
        // fromのユーザのフォロイー
        val followings = followRepository.fetchByFollowerId(from).map { it.followee }

        return to.map {
            FollowState(
                selfId = from,
                otherId = it,
                following = followings.contains(it),
                followed = followers.contains(it)
            )
        }
    }

}
