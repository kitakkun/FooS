package com.example.foos.ui.view.screen.userprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.foos.data.domain.GetPostsByUserIdUseCase
import com.example.foos.data.domain.GetUserInfoUseCase
import com.example.foos.data.model.Post
import com.example.foos.data.domain.*
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
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
    private val getPostsWithUserByUserIdWithDateUseCase: GetPostsWithUserByUserIdWithDateUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(UserProfileScreenUiState.Default)
    val uiState: StateFlow<UserProfileScreenUiState> get() = _uiState

    private var _navigateRouteFlow = MutableSharedFlow<String>()
    val navigateRouteFlow: SharedFlow<String> get() = _navigateRouteFlow

    private var _scrollUpEvent = MutableSharedFlow<Boolean>()
    val scrollUpEvent = _scrollUpEvent.asSharedFlow()

    fun onUserIconClick() {
        viewModelScope.launch {
            _scrollUpEvent.emit(true)
        }
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param uiState クリックされた投稿のUI状態
     */
    fun onContentClick(uiState: PostItemUiState) {
        val data = Uri.encode(Gson().toJson(uiState))
        viewModelScope.launch {
            _navigateRouteFlow.emit("${Page.PostDetail.route}/$data")
        }
    }

    /**
     * 投稿コンテンツの画像クリック時のイベント
     * @param uiState クリックされた画像を持つ投稿のUI状態
     * @param clickedImageUrl クリックされた画像のURL
     */
    fun onImageClick(uiState: PostItemUiState, clickedImageUrl: String) {
        val uiStateWithImageUrl =
            PostItemUiStateWithImageUrl(uiState, uiState.attachedImages.indexOf(clickedImageUrl))
        val data = Uri.encode(Gson().toJson(uiStateWithImageUrl))
        viewModelScope.launch {
            _navigateRouteFlow.emit("${Page.ImageDetail.route}/$data")
        }
    }

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