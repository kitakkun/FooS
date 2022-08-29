package com.example.foos.ui.view.screen.followlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.FetchFolloweesWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.FetchFollowersWithMyFollowStateByUserIdUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.state.screen.followlist.FollowListScreenUiState
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.view.screen.Page
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchFollowersWithMyFollowStateByUserIdUseCase: FetchFollowersWithMyFollowStateByUserIdUseCase,
    private val fetchFolloweesWithMyFollowStateByUserIdUseCase: FetchFolloweesWithMyFollowStateByUserIdUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FollowListScreenUiState(listOf(), listOf()))
    val uiState = _uiState.asStateFlow()

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun navigateToUserProfile(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${Page.UserProfile.route}/$userId")
        }
    }

    fun fetchFollowees(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val followees = fetchFolloweesWithMyFollowStateByUserIdUseCase(Firebase.auth.uid!!, userId).map {
                UserItemUiState(
                    username = it.user.username,
                    profileImage = it.user.profileImage,
                    userId = it.user.userId,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    bio = "BIO",
                    following = it.followState.following,
                    followingYou = it.followState.followed,
                )
            }
            _uiState.update { it.copy(followees = it.followees + followees) }
        }
    }

    fun fetchFollowers(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val followers = fetchFollowersWithMyFollowStateByUserIdUseCase(Firebase.auth.uid!!, userId).map {
                UserItemUiState(
                    username = it.user.username,
                    profileImage = it.user.profileImage,
                    userId = it.user.userId,
                    // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                    bio = "BIO",
                    following = it.followState.following,
                    followingYou = it.followState.followed,
                )
            }
            _uiState.update { it.copy(followers = it.followers + followers) }
        }
    }

}