package com.example.foos.ui.view.screen.userprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.*
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ユーザプロフィール画面のViewModel
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val getPostsWithUserByUserIdWithDateUseCase: GetPostsWithUserByUserIdWithDateUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(UserProfileScreenUiState.Default)
    val uiState: StateFlow<UserProfileScreenUiState> get() = _uiState

    fun setUserId(userId: String) {
        viewModelScope.launch {
            fetchPosts(userId)
            fetchUserInfo(userId)
        }
    }

    suspend fun fetchUserInfo(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.fetchByUserId(userId)
            user?.let {
                _uiState.update {
                    it.copy(
                        userId = user.userId,
                        userIcon = user.profileImage,
                        username = user.username,
                    )
                }
            }
        }
    }

    suspend fun fetchPosts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = getPostsWithUserByUserIdWithDateUseCase(userId).map {
                convertPostWithUserToUiStateUseCase(it)
            }
            _uiState.update { it.copy(posts = posts) }
        }
    }

    /**
     * 古い投稿をフェッチします
     */
    fun fetchOlderPosts() {
        viewModelScope.launch {
            val oldestPost = uiState.value.posts.last()
            val oldestDate = oldestPost.createdAt
            oldestDate?.let {
                val posts = getPostsWithUserByUserIdWithDateUseCase(uiState.value.userId, null, oldestDate).map {
                    convertPostWithUserToUiStateUseCase(it)
                }
                _uiState.update { state ->
                    state.copy(posts = (state.posts + posts).distinct())
                }
            }
        }
    }

}