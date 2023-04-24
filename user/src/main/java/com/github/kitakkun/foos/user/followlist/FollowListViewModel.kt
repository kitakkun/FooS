package com.github.kitakkun.foos.user.followlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.model.DatabaseUser
import com.github.kitakkun.foos.common.model.follow.FollowGraph
import com.github.kitakkun.foos.common.model.follow.FollowState
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchFollowStateUseCase
import com.github.kitakkun.foos.user.com.github.kitakkun.foos.user.followlist.FollowUserUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class FollowListViewModel(
    private val userId: String,
    shouldShowFollowingListFirst: Boolean,
    private val auth: FirebaseAuth,
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchFollowStateUseCase: FetchFollowStateUseCase,
) : ViewModel() {
    private var mutableUiState = MutableStateFlow(
        FollowListScreenUiState(
            shouldShowFollowingListFirst = shouldShowFollowingListFirst,
        )
    )
    val uiState = mutableUiState.asStateFlow()

    fun fetchFollowingUsers(isRefresh: Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
        val clientId = auth.uid ?: return@launch

        mutableUiState.update { uiState ->
            uiState.copy(isFollowingListRefreshing = isRefresh)
        }

        val followingGraphs = followRepository.fetchFollowingGraphs(userId)
        val followingUserIds = followingGraphs.map { it.to }
        val followingUsersInfo = usersRepository.fetchByUserIds(followingUserIds)
        val clientFollowStateBetweenUsers = fetchFollowStateUseCase(clientId, followingUserIds)
        // UIStateの作成
        val userItems = createFollowUserUiStates(
            clientId = clientId,
            followGraphs = followingGraphs,
            followUsersInfo = followingUsersInfo,
            clientFollowStateBetweenUsers = clientFollowStateBetweenUsers,
        )

        mutableUiState.update { state ->
            state.copy(
                isFollowingListRefreshing = false,
                followingUsers = when (isRefresh) {
                    true -> userItems
                    false -> (uiState.value.followingUsers + userItems).distinctBy { it.id }
                }
            )
        }
    }

    private fun createFollowUserUiStates(
        clientId: String,
        followGraphs: List<FollowGraph>,
        followUsersInfo: List<DatabaseUser>,
        clientFollowStateBetweenUsers: List<FollowState>,
    ): List<FollowUserUiState> = followGraphs.mapNotNull { followingGraph ->
        val userInfo = followUsersInfo.find { it.id == followingGraph.to }
            ?: return@mapNotNull null
        val followState =
            clientFollowStateBetweenUsers.find { it.otherId == followingGraph.to }
                ?: return@mapNotNull null
        createFollowUserUiState(
            clientId = clientId,
            userInfo = userInfo,
            followState = followState,
            followingGraph = followingGraph,
        )
    }

    private fun createFollowUserUiState(
        clientId: String,
        userInfo: DatabaseUser,
        followState: FollowState,
        followingGraph: FollowGraph,
    ) = FollowUserUiState(
        id = userInfo.id,
        name = userInfo.name,
        profileImageUrl = userInfo.profileImage,
        isFollowButtonVisible = userInfo.id != clientId,
        biography = "BIO",
        isFollowedByClientUser = followState.following,
        isFollowsYouVisible = followState.followed,
    )

    fun fetchFollowerUsers(isRefresh: Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
        val clientId = auth.uid ?: return@launch

        mutableUiState.update { it.copy(isFollowerListRefreshing = isRefresh) }

        val followerGraphs = followRepository.fetchFollowerGraphs(userId)
        val profileUserFollowerIds = followerGraphs.map { it.from }
        val profileUserFollowers = usersRepository.fetchByUserIds(profileUserFollowerIds)
        val myFollowStates = fetchFollowStateUseCase(clientId, profileUserFollowerIds)
        val userItems = createFollowUserUiStates(
            followGraphs = followerGraphs,
            followUsersInfo = profileUserFollowers,
            clientFollowStateBetweenUsers = myFollowStates,
            clientId = clientId,
        )
        // UIStateの作成
        mutableUiState.update { state ->
            state.copy(
                isFollowerListRefreshing = false,
                followers = when (isRefresh) {
                    true -> userItems
                    false -> (uiState.value.followers + userItems).distinctBy { it.id }
                }
            )
        }
    }

    fun fetchMoreFollowingUsers() = viewModelScope.launch(Dispatchers.IO) {
        val lastFollowingUserCreatedAt = getLastUserFollowingCreatedAt(uiState.value.followingUsers)
        val fetchedUserIds = followRepository.fetchFollowingGraphs(
            userId = userId,
            olderThan = lastFollowingUserCreatedAt
        ).map { it.to }
        val fetchedUsers = usersRepository.fetchByUserIds(fetchedUserIds)
        val fetchedFollowStates = fetchFollowStateUseCase(
            from = auth.uid ?: return@launch,
            to = fetchedUserIds
        )
        val userItems = createFollowUserUiStates(
            clientId = auth.uid ?: return@launch,
            followGraphs = followRepository.fetchFollowingGraphs(userId),
            followUsersInfo = fetchedUsers,
            clientFollowStateBetweenUsers = fetchedFollowStates,
        )
        mutableUiState.update { state ->
            state.copy(
                isFollowerListRefreshing = false,
                followers = (uiState.value.followingUsers + userItems).distinctBy { it.id }
            )
        }
    }

    private suspend fun getLastUserFollowingCreatedAt(
        userList: List<FollowUserUiState>,
    ): Date? {
        val lastUser = userList.lastOrNull() ?: return null
        val lastFollowGraph = followRepository.fetchFollowGraph(
            from = lastUser.id,
            to = userId,
        )
        return lastFollowGraph?.createdAt
    }

    fun fetchMoreFollowerUsers() = viewModelScope.launch(Dispatchers.IO) {
        val lastFetchedFollowerUserId = uiState.value.followers.lastOrNull()?.id ?: return@launch
        val lastFollowerGraphCreatedAt = followRepository.fetchFollowGraph(
            from = lastFetchedFollowerUserId,
            to = userId,
        )?.createdAt ?: return@launch
        val fetchedUserIds = followRepository.fetchFollowerGraphs(
            userId = userId,
            olderThan = lastFollowerGraphCreatedAt,
        ).map { it.to }
        val fetchedUsers = usersRepository.fetchByUserIds(fetchedUserIds)
        val fetchedFollowStates = fetchFollowStateUseCase(
            from = auth.uid ?: return@launch,
            to = fetchedUserIds
        )
        val userItems = createFollowUserUiStates(
            clientId = auth.uid ?: return@launch,
            followGraphs = followRepository.fetchFollowingGraphs(userId),
            followUsersInfo = fetchedUsers,
            clientFollowStateBetweenUsers = fetchedFollowStates,
        )
        mutableUiState.update { state ->
            state.copy(
                isFollowerListRefreshing = false,
                followers = (uiState.value.followers + userItems).distinctBy { it.id }
            )
        }
    }

    fun toggleFollowState(targetUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUiState = uiState.value
            val clientId = auth.uid ?: return@launch

            val followedByClientUsers =
                (currentUiState.followers + currentUiState.followingUsers).filter { it.isFollowedByClientUser }
            val isFollowedByClient = followedByClientUsers.any { it.id == targetUserId }

            if (isFollowedByClient) {
                followRepository.deleteFollowGraph(from = clientId, to = targetUserId)
            } else {
                followRepository.createFollowGraph(from = clientId, to = targetUserId)
            }

            val newFollowerList = currentUiState.followers.map {
                if (it.id == targetUserId) it.copy(isFollowedByClientUser = !isFollowedByClient)
                else it
            }
            val newFollowingList = currentUiState.followingUsers.map {
                if (it.id == targetUserId) it.copy(isFollowedByClientUser = !isFollowedByClient)
                else it
            }

            mutableUiState.update {
                it.copy(
                    followers = newFollowerList, followingUsers = newFollowingList
                )
            }
        }
    }
}
