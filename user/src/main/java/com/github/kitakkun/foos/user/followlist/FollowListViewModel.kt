package com.github.kitakkun.foos.user.followlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchFollowStateUseCase
import com.github.kitakkun.foos.user.composable.UserItemUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun fetchFollowingUsers(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.update { uiState ->
                uiState.copy(isFollowingListRefreshing = isRefresh)
            }
            val clientId = auth.uid ?: return@launch
            val profileUserFollowingUserIds = followRepository.fetchFollowingUserIds(userId)
            val profileUserFollowingUsersInfo =
                usersRepository.fetchByUserIds(profileUserFollowingUserIds)
            val myFollowStatesToFollowingUsers =
                fetchFollowStateUseCase(clientId, profileUserFollowingUserIds)
            // UIStateの作成
            val userItems = profileUserFollowingUserIds.mapNotNull { followingUserId ->
                val userInfo =
                    profileUserFollowingUsersInfo.find { it.id == followingUserId }
                        ?: return@mapNotNull null
                val followState =
                    myFollowStatesToFollowingUsers.find { it.otherId == followingUserId }
                        ?: return@mapNotNull null
                UserItemUiState(
                    isFollowButtonVisible = userInfo.id != clientId,
                    name = userInfo.name,
                    profileImageUrl = userInfo.profileImage,
                    id = userInfo.id,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    biography = "BIO",
                    isFollowedByClient = followState.following,
                    isFollowsYouVisible = followState.followed,
                )
            }
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
    }

    fun fetchFollowerUsers(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.update { it.copy(isFollowerListRefreshing = isRefresh) }
            val clientId = auth.uid ?: return@launch
            // プロフィール画面のユーザのフォロワーをフェッチ
            val profileUserFollowerIds = followRepository.fetchFollowerUserIds(userId)
            // フォロワーのユーザ情報をフェッチ
            val profileUserFollowers = usersRepository.fetchByUserIds(profileUserFollowerIds)
            // 各フォロワーと自分とのフォロー関係をフェッチ
            val myFollowStates = fetchFollowStateUseCase(clientId, profileUserFollowerIds)
            // UIStateの作成
            val userItems = profileUserFollowerIds.mapNotNull { followerId ->
                val user =
                    profileUserFollowers.find { it.id == followerId } ?: return@mapNotNull null
                val followState =
                    myFollowStates.find { it.otherId == followerId } ?: return@mapNotNull null
                UserItemUiState(
                    isFollowButtonVisible = user.id != clientId,
                    name = user.name,
                    profileImageUrl = user.profileImage,
                    id = user.id,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    biography = "BIO",
                    isFollowedByClient = followState.following,
                    isFollowsYouVisible = followState.followed,
                )
            }
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
    }

    fun toggleFollowState(targetUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUiState = uiState.value
            val clientId = auth.uid ?: return@launch

            val followedByClientUsers =
                (currentUiState.followers + currentUiState.followingUsers).filter { it.isFollowedByClient }
            val isFollowedByClient = followedByClientUsers.any { it.id == targetUserId }

            if (isFollowedByClient) {
                followRepository.deleteFollowGraph(from = clientId, to = targetUserId)
            } else {
                followRepository.createFollowGraph(from = clientId, to = targetUserId)
            }

            val newFollowerList = currentUiState.followers.map {
                if (it.id == targetUserId) it.copy(isFollowedByClient = !isFollowedByClient)
                else it
            }
            val newFollowingList = currentUiState.followingUsers.map {
                if (it.id == targetUserId) it.copy(isFollowedByClient = !isFollowedByClient)
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
