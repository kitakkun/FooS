package com.example.foos.ui.view.screen.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.GetLatestPostsWithUserUseCase
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.state.screen.home.HomeScreenUiState
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.screen.Page
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeScreenに対応するViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestPostsWithUserUseCase: GetLatestPostsWithUserUseCase,
) : ViewModel() {

    // HomeScreenのUI状態
    private var _uiState = MutableStateFlow(HomeScreenUiState(listOf(), false))
    val uiState: StateFlow<HomeScreenUiState> get() = _uiState

    private val _navEvent = MutableSharedFlow<String>()
    val navEvent: SharedFlow<String> get() = _navEvent

    fun onAppearLastItem(count: Int) {
        val lastItemCreatedAt = uiState.value.posts.last().createdAt
        viewModelScope.launch {
            lastItemCreatedAt?.let {
                val postsWithUser = getLatestPostsWithUserUseCase.invoke(it)
                val posts = postsWithUser.map {
                    PostItemUiState(
                        postId = it.databasePost.postId,
                        userId = it.databaseUser.userId,
                        username = it.databaseUser.username,
                        userIcon = it.databaseUser.profileImage,
                        content = it.databasePost.content,
                        attachedImages = it.databasePost.attachedImages,
                        latitude = it.databasePost.latitude,
                        longitude = it.databasePost.longitude,
                        createdAt = it.databasePost.createdAt,
                    )
                }
                _uiState.update {
                    it.copy(
                        posts = (it.posts + posts).distinct(),
                    )
                }
            }
        }
    }


    /**
     * ユーザアイコンのクリックイベント
     * @param userId クリックされたユーザのID
     */
    fun onUserIconClick(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${Page.UserProfile.route}/$userId")
        }
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param uiState クリックされた投稿のUI状態
     */
    fun onContentClick(uiState: PostItemUiState) {
        viewModelScope.launch {
            val data = Uri.encode(Gson().toJson(uiState))
            _navEvent.emit("${Page.PostDetail.route}/$data")
        }
    }

    /**
     * 投稿コンテンツの画像クリック時のイベント
     * @param uiState クリックされた画像を持つ投稿のUI状態
     * @param clickedImageUrl クリックされた画像のURL
     */
    fun onImageClick(uiState: PostItemUiState, clickedImageUrl: String) {
        viewModelScope.launch {
            val uiStateWithImageUrl =
                PostItemUiStateWithImageUrl(
                    uiState,
                    uiState.attachedImages.indexOf(clickedImageUrl)
                )
            val data = Uri.encode(Gson().toJson(uiStateWithImageUrl))
            _navEvent.emit("${Page.ImageDetail.route}/$data")
        }
    }

    fun fetchNewPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isRefreshing = true) }
            val postsWithUser = getLatestPostsWithUserUseCase()
            val posts = postsWithUser.map {
                PostItemUiState(
                    postId = it.databasePost.postId,
                    userId = it.databaseUser.userId,
                    username = it.databaseUser.username,
                    userIcon = it.databaseUser.profileImage,
                    content = it.databasePost.content,
                    attachedImages = it.databasePost.attachedImages,
                    latitude = it.databasePost.latitude,
                    longitude = it.databasePost.longitude,
                    createdAt = it.databasePost.createdAt,
                )
            }
            _uiState.update {
                it.copy(
                    posts = (posts + it.posts).distinct(),
                    isRefreshing = false
                )
            }
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
//            postsRepository.fetchOlderPosts()
        }
    }


}