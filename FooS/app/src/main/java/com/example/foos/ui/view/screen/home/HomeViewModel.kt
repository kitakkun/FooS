package com.example.foos.ui.view.screen.home

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.fetcher.post.FetchPostsUseCase
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.navigation.navargs.StringList
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.state.screen.home.HomeScreenUiState
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
    private val fetchPostsUseCase: FetchPostsUseCase,
) : ViewModel() {

    // HomeScreenのUI状態
    private var _uiState = mutableStateOf(HomeScreenUiState.Default)
    val uiState: State<HomeScreenUiState> = _uiState

    private val _navEvent = MutableSharedFlow<String>()
    val navEvent: SharedFlow<String> get() = _navEvent

    /**
     * ユーザアイコンのクリックイベント
     * @param userId クリックされたユーザのID
     */
    fun onUserIconClick(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${SubScreen.UserProfile.route}/$userId")
        }
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param postId クリックされた投稿のID
     */
    fun onContentClick(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostDetail.route(postId))
        }
    }

    /**
     * 投稿コンテンツの画像クリック時のイベント
     * @param uiState クリックされた画像を持つ投稿のUI状態
     * @param clickedImageUrl クリックされた画像のURL
     */
    fun onImageClick(imageUrls: List<String>, clickedImageUrl: String) {
        viewModelScope.launch {
            _navEvent.emit(
                SubScreen.ImageDetail.route(
                    Uri.encode(Gson().toJson(StringList(imageUrls))),
                    imageUrls.indexOf(clickedImageUrl).toString()
                ),
            )
        }
    }

    /**
     * 新しい投稿をフェッチします（リフレッシュ）
     */
    fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsUseCase().map { PostItemUiState.convert(it) }
            _uiState.value = uiState.value.copy(posts = posts, isRefreshing = false)
        }
    }

    /**
     * 初回フェッチ
     */
    fun fetchInitialPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val posts = fetchPostsUseCase().map { post ->
                PostItemUiState.convert(post)
            }
            _uiState.value = uiState.value.copy(
                isLoading = false,
                posts = (posts + uiState.value.posts).distinct()
            )
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
                val posts = fetchPostsUseCase(end = it).map { post ->
                    PostItemUiState.convert(post)
                }
                _uiState.value =
                    uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }


}