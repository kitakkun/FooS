package com.example.foos.ui.view.screen.followlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.model.DatabaseUser
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.state.screen.followlist.FollowListScreenUiState
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FollowListScreenUiState(listOf()))
    val uiState = _uiState.asStateFlow()

    suspend fun fetchFollowers(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jobs = mutableListOf<Job>()
            val users = mutableListOf<UserItemUiState>()
            followRepository.fetchFollowers(userId).forEach {
                jobs.add(async {
                    usersRepository.fetchByUserId(it.follower)?.let { user ->
                        users.add(
                            UserItemUiState(
                                username = user.username,
                                profileImage = user.profileImage,
                                userId = user.userId,
                                // TODO: ユーザのデータベースデータの拡張とフォロー関係の取得
                                bio = "BIO",
                                following = true,
                                followingYou = true,
                            )
                        )
                    }
                })
            }
            jobs.joinAll()
            _uiState.update { it.copy(users = it.users + users) }
        }
    }

    suspend fun fetchFollowees(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val jobs = mutableListOf<Job>()
            val users = mutableListOf<DatabaseUser>()
            followRepository.fetchFollowees(userId).forEach {
                jobs.add(async {
                    usersRepository.fetchByUserId(it.follower)?.let { user ->
                        users.add(user)
                    }
                })
            }
            jobs.joinAll()
        }
    }
}