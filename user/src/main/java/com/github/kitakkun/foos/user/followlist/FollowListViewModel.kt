package com.github.kitakkun.foos.user.followlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchFollowStateUseCase
import com.github.kitakkun.foos.user.composable.UserItemUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchFollowStateUseCase: FetchFollowStateUseCase,
) : ViewModel() {

    private var mutableUiState = MutableStateFlow(FollowListScreenUiState())
    val uiState = mutableUiState.asStateFlow()

    fun fetchFollowingUsers(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val clientId = auth.uid ?: return@launch
            // プロフィール画面のユーザのフォロイーをフェッチ
            val followees = followRepository.fetchByFollowerId(userId).map { it.to }
            // フォロイーのユーザ情報をフェッチ
            val users = usersRepository.fetchByUserIds(followees)
            // 各フォロイーと自分とのフォロー関係をフェッチ
            val myFollowStates = fetchFollowStateUseCase(clientId, followees)
            // UIStateの作成
            val userItems = followees.mapNotNull { followeeId ->
                val user = users.find { it.id == followeeId }
                val followState = myFollowStates.find { it.otherId == followeeId }
                if (user == null || followState == null) null
                else UserItemUiState(
                    isFollowButtonVisible = user.id == clientId,
                    name = user.name,
                    profileImageUrl = user.profileImage,
                    id = user.id,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    biography = "BIO",
                    isFollowedByClient = followState.following,
                    isFollowsYouVisible = followState.followed,
                )
            }
            mutableUiState.value =
                uiState.value.copy(followingUsers = (uiState.value.followingUsers + userItems).distinct())
        }
    }

    fun fetchFollowerUsers(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val clientId = auth.uid ?: return@launch
            // プロフィール画面のユーザのフォロワーをフェッチ
            val followers = followRepository.fetchByFolloweeId(userId).map { it.from }
            // フォロワーのユーザ情報をフェッチ
            val users = usersRepository.fetchByUserIds(followers)
            // 各フォロワーと自分とのフォロー関係をフェッチ
            val myFollowStates = fetchFollowStateUseCase(clientId, followers)
            // UIStateの作成
            val userItems = followers.mapNotNull { followerId ->
                val user = users.find { it.id == followerId }
                val followState = myFollowStates.find { it.otherId == followerId }
                if (user == null || followState == null) null
                else UserItemUiState(
                    isFollowButtonVisible = user.id == clientId,
                    name = user.name,
                    profileImageUrl = user.profileImage,
                    id = user.id,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    biography = "BIO",
                    isFollowedByClient = followState.following,
                    isFollowsYouVisible = followState.followed,
                )
            }
            mutableUiState.value =
                uiState.value.copy(followers = (uiState.value.followers + userItems).distinct())
            Log.d("TAG", "fetch")
        }
    }
}
