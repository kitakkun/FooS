package com.github.kitakkun.foos.common.usecase

import com.github.kitakkun.foos.common.model.follow.FollowState
import com.github.kitakkun.foos.common.repository.FollowRepository

class FetchFollowStateUseCase(
    private val followRepository: FollowRepository,
) {
    suspend operator fun invoke(from: String, to: String): FollowState {
        val followingToUser = followRepository.fetchFollowGraph(from = from, to = to) != null
        val followedByToUser = followRepository.fetchFollowGraph(from = to, to = from) != null
        return FollowState(
            selfId = from,
            otherId = to,
            following = followingToUser,
            followed = followedByToUser,
        )
    }

    suspend operator fun invoke(from: String, to: List<String>): List<FollowState> {
        // fromのユーザのフォロワー
        val followers = followRepository.fetchFollowerGraphs(from).map { it.from }
        // fromのユーザのフォロイー
        val followings = followRepository.fetchFollowingGraphs(from).map { it.to }

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
