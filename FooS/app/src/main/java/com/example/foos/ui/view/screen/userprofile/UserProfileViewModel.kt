package com.example.foos.ui.view.screen.userprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.foos.data.domain.GetPostsByUserIdUseCase
import com.example.foos.data.domain.GetUserInfoUseCase
import com.example.foos.data.model.Post
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import com.example.foos.ui.view.screen.Page
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    private var _navigateRouteFlow = MutableSharedFlow<String>()
    val navigateRouteFlow: SharedFlow<String> get() = _navigateRouteFlow

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
            Log.d("FETCHLOG", "Instance is $this")
            Log.d("FETCHLOG", "fetching started")
            val posts = getPostsByUserIdUseCase(userId).map {
                PostItemUiState(
                    postId = it.post.postId,
                    userId = it.user.userId,
                    username = it.user.username,
                    userIcon = it.user.profileImage,
                    content = it.post.content,
                    attachedImages = it.post.attachedImages,
                )
            }
            _uiState.update { it.copy(posts = posts) }
            Log.d("FETCHLOG", "fetching finished")
        }
    }

    /**
     * 投稿内容がクリックされたときの挙動
     */
    fun onContentClicked(uiState: PostItemUiState) {
        val data = Uri.encode(Gson().toJson(uiState))
        viewModelScope.launch {
            _navigateRouteFlow.emit("${Page.PostDetail.route}/$data")
        }
    }
}