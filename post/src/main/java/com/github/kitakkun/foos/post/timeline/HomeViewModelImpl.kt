package com.github.kitakkun.foos.post.timeline

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.navigation.BottomSheet
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.common.usecase.FetchPostsUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeScreenに対応するViewModel
 */
@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val fetchPostsUseCase: FetchPostsUseCase,
) : ViewModel(), HomeViewModel {

    // HomeScreenのUI状態
    private var _uiState = mutableStateOf(HomeScreenUiState.Default)
    override val uiState: State<HomeScreenUiState> = _uiState

    private val _navEvent = MutableSharedFlow<String>()
    override val navEvent: SharedFlow<String> get() = _navEvent

    /**
     * 投稿作成画面へ遷移
     */
    override fun onPostCreateButtonClick() {
        viewModelScope.launch {
            _navEvent.emit(PostScreenRouter.PostCreate.route)
        }
    }

    /**
     * ユーザアイコンのクリックイベント
     * @param userId クリックされたユーザのID
     */
    override fun onUserIconClick(userId: String) {
        viewModelScope.launch {
            _navEvent.emit(UserScreenRouter.UserProfile.routeWithArgs(userId))
        }
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param postId クリックされた投稿のID
     */
    override fun onContentClick(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(PostScreenRouter.Detail.PostDetail.routeWithArgs(postId))
        }
    }

    override fun onImageClick(imageUrls: List<String>, clickedImageUrl: String) {
        viewModelScope.launch {
            _navEvent.emit(
                PostScreenRouter.Detail.ImageDetail.routeWithArgs(
                    Uri.encode(Gson().toJson(StringList(imageUrls))),
                    imageUrls.indexOf(clickedImageUrl).toString()
                ),
            )
        }
    }

    /**
     * 新しい投稿をフェッチします（リフレッシュ）
     */
    override fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsUseCase().map { PostItemUiState.convert(it) }
            _uiState.value = uiState.value.copy(posts = posts, isRefreshing = false)
        }
    }

    /**
     * 初回フェッチ
     */
    override fun fetchInitialPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            Log.d("SIZE", fetchPostsUseCase().size.toString())
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
    override fun fetchOlderPosts() {
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

    override fun onMoreVertClick(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(BottomSheet.PostOption.route(postId))
        }
    }

    override fun dispose() {
        viewModelScope.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel", "onCleared")
    }
}
