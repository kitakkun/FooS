package com.example.foos.ui.view.screen.home

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.GetPostsWithUserUseCase
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.state.screen.home.HomeScreenUiState
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.screen.Page
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeScreenに対応するViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsWithUserUseCase: GetPostsWithUserUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase
) : ViewModel() {

    // HomeScreenのUI状態
    private var _uiState = mutableStateOf(HomeScreenUiState(listOf(), false))
    val uiState: State<HomeScreenUiState> = _uiState

    private val _navEvent = MutableSharedFlow<String>()
    val navEvent: SharedFlow<String> get() = _navEvent

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

    /**
     * 新しい投稿をフェッチします（リフレッシュ）
     */
    fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts = getPostsWithUserUseCase().map { postWithUser ->
                convertPostWithUserToUiStateUseCase(postWithUser)
            }
            _uiState.value = uiState.value.copy(posts = posts, isRefreshing = false)
        }
    }

    /**
     * 新しい投稿をフェッチします（サイレント）
     */
    fun fetchNewerPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = getPostsWithUserUseCase().map { postWithUser ->
                convertPostWithUserToUiStateUseCase(postWithUser)
            }
            _uiState.value = uiState.value.copy(posts = (posts + uiState.value.posts).distinct())
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
                val posts = getPostsWithUserUseCase(it).map { postWithUser ->
                    convertPostWithUserToUiStateUseCase(postWithUser)
                }
                _uiState.value = uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }


}