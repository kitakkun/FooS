package com.example.foos.ui.view.screen.userprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.GetPostsByUserIdUseCase
import com.example.foos.data.domain.GetUserInfoUseCase
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getPostsByUserIdUseCase: GetPostsByUserIdUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(UserProfileScreenUiState.Default)
    val uiState: StateFlow<UserProfileScreenUiState> get() = _uiState

    fun setUserId(userId: String) {
        viewModelScope.launch {
            fetchUserPosts(userId)
            fetchUserInfo(userId)
        }
    }

    suspend fun fetchUserInfo(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.fetchUser(userId)
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

    suspend fun fetchUserPosts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = getPostsByUserIdUseCase(userId).map {
                PostItemUiState(
                    postId = it.post.postId,
                    userId = it.user.userId,
                    username = it.user.username,
                    userIcon = it.user.profileImage,
                    content = it.post.content,
                    attachedImages = it.post.attachedImages,
                    createdAt = it.post.createdAt
                )
            }
            _uiState.update { it.copy(posts = posts) }
            Log.d("FETCHLOG", "fetching finished")
        }
    }

}