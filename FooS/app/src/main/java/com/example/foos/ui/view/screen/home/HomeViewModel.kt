package com.example.foos.ui.view.screen.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.foos.data.domain.GetLatestPostsWithUserUseCase
import com.example.foos.data.domain.GetOlderPostsWithUserUseCase
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.state.screen.home.HomeScreenUiState
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.screen.Page
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeScreenに対応するViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestPostsWithUserUseCase: GetLatestPostsWithUserUseCase,
    private val getOlderPostsWithUserUseCase: GetOlderPostsWithUserUseCase,
    application: Application,
) : AndroidViewModel(application) {

    // HomeScreenのUI状態
    private var _uiState = MutableStateFlow(HomeScreenUiState(listOf(), false))
    val uiState: StateFlow<HomeScreenUiState> get() = _uiState

    private lateinit var navController: NavController

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    /**
     * ユーザアイコンのクリックイベント
     * @param userId クリックされたユーザのID
     */
    fun onUserIconClick(userId: String) {
        navController.navigate("${Page.UserProfile.route}/$userId")
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param uiState クリックされた投稿のUI状態
     */
    fun onContentClick(uiState: PostItemUiState) {
        val data = Uri.encode(Gson().toJson(uiState))
        navController.navigate("${Page.PostDetail.route}/$data")
    }

    /**
     * 投稿コンテンツの画像クリック時のイベント
     * @param uiState クリックされた画像を持つ投稿のUI状態
     * @param clickedImageUrl クリックされた画像のURL
     */
    fun onImageClick(uiState: PostItemUiState, clickedImageUrl: String) {
        val uiStateWithImageUrl = PostItemUiStateWithImageUrl(uiState, clickedImageUrl)
        val data = Uri.encode(Gson().toJson(uiStateWithImageUrl))
        navController.navigate("${Page.ImageDetail.route}/$data")
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