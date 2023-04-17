package com.github.kitakkun.foos.user.followlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchFollowStateUseCase
import com.github.kitakkun.foos.user.composable.UserItemUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowListViewModelImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchFollowStateUseCase: FetchFollowStateUseCase,
) : ViewModel(), FollowListViewModel {

    private var _uiState = mutableStateOf(FollowListScreenUiState())
    override val uiState: State<FollowListScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    override val navEvent = _navEvent.asSharedFlow()

    override fun navigateToUserProfile(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${SubScreen.UserProfile.route}/$userId")
        }
    }

    override fun fetchFollowees(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val clientId = auth.uid ?: return@launch
            // プロフィール画面のユーザのフォロイーをフェッチ
            val followees = followRepository.fetchByFollowerId(userId).map { it.followee }
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
            _uiState.value =
                uiState.value.copy(followingUsers = (uiState.value.followingUsers + userItems).distinct())
        }
    }

    override fun fetchFollowers(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val clientId = auth.uid ?: return@launch
            // プロフィール画面のユーザのフォロワーをフェッチ
            val followers = followRepository.fetchByFolloweeId(userId).map { it.follower }
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
            _uiState.value =
                uiState.value.copy(followers = (uiState.value.followers + userItems).distinct())
            Log.d("TAG", "fetch")
        }
    }

}
