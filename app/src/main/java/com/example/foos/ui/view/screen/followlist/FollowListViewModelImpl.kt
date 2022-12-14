package com.example.foos.ui.view.screen.followlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.fetcher.follow.FetchFollowStateUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.state.screen.followlist.FollowListScreenUiState
import com.example.foos.ui.state.screen.followlist.UserItemUiState
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

    private var _uiState = mutableStateOf(FollowListScreenUiState(listOf(), listOf()))
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
                val user = users.find { it.userId == followeeId }
                val followState = myFollowStates.find { it.otherId == followeeId }
                if (user == null || followState == null) null
                else UserItemUiState(
                    clientUserId = clientId,
                    username = user.username,
                    profileImage = user.profileImage,
                    userId = user.userId,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    bio = "BIO",
                    following = followState.following,
                    followingYou = followState.followed,
                )
            }
            _uiState.value =
                uiState.value.copy(followees = (uiState.value.followees + userItems).distinct())
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
                val user = users.find { it.userId == followerId }
                val followState = myFollowStates.find { it.otherId == followerId }
                if (user == null || followState == null) null
                else UserItemUiState(
                    clientUserId = clientId,
                    username = user.username,
                    profileImage = user.profileImage,
                    userId = user.userId,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    bio = "BIO",
                    following = followState.following,
                    followingYou = followState.followed,
                )
            }
            _uiState.value =
                uiState.value.copy(followers = (uiState.value.followers + userItems).distinct())
            Log.d("TAG", "fetch")
        }
    }

}