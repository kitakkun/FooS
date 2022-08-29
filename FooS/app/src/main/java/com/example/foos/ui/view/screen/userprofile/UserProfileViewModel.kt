package com.example.foos.ui.view.screen.userprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.GetPostsWithUserByUserIdWithDateUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import com.example.foos.ui.view.screen.Page
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
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
    private val followRepository: FollowRepository,
    private val getPostsWithUserByUserIdWithDateUseCase: GetPostsWithUserByUserIdWithDateUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(UserProfileScreenUiState.Default)
    val uiState: StateFlow<UserProfileScreenUiState> get() = _uiState

    // ナビゲーションイベント
    private var _navEvent = MutableSharedFlow<String>()
    val navEvent: SharedFlow<String> get() = _navEvent

    private var _scrollUpEvent = MutableSharedFlow<Boolean>()
    val scrollUpEvent = _scrollUpEvent.asSharedFlow()

    /**
     * フォローボタンクリック時の挙動
     */
    fun onFollowButtonClick() {
        val following = uiState.value.following
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.auth.uid?.let {
                if (!following) followRepository.create(it, uiState.value.userId)
                else followRepository.delete(it, uiState.value.userId)
                val followee = followRepository.fetchFollowees(uiState.value.userId)
                val follower = followRepository.fetchFollowers(uiState.value.userId)
                _uiState.update {
                    it.copy (
                        followerCount = follower.size,
                        followeeCount = followee.size,
                        following = !following
                    )
                }
            }
        }
    }

    /**
     * フォロワーリストのページへ遷移します
     */
    fun navigateToFollowerList(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${Page.FollowList.route}/$userId/${false}")
        }
    }

    /**
     * フォロイーリストのページへ遷移します
     */
    fun navigateToFolloweeList(userId: String) {
        viewModelScope.launch {
            _navEvent.emit("${Page.FollowList.route}/$userId/${true}")
        }
    }

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
            _navEvent.emit("${Page.PostDetail.route}/$data")
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
            _navEvent.emit("${Page.ImageDetail.route}/$data")
        }
    }

    fun setUserId(userId: String) {
        viewModelScope.launch {
            fetchPosts(userId)
            fetchUserInfo(userId)
        }
    }

    private suspend fun fetchUserInfo(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val followers = followRepository.fetchFollowers(userId)
            val followees = followRepository.fetchFollowees(userId)
            val user = usersRepository.fetchByUserId(userId)
            user?.let {
                _uiState.update {
                    it.copy(
                        userId = user.userId,
                        userIcon = user.profileImage,
                        username = user.username,
                        followeeCount = followees.size,
                        followerCount = followers.size,
                        following = followers.map { followInfo -> followInfo.follower }
                            .contains(Firebase.auth.uid.toString())
                    )
                }
            }
        }
    }

    private suspend fun fetchPosts(userId: String) {
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
                val posts = getPostsWithUserByUserIdWithDateUseCase(
                    uiState.value.userId,
                    null,
                    oldestDate
                ).map {
                    convertPostWithUserToUiStateUseCase(it)
                }
                _uiState.update { state ->
                    state.copy(posts = (state.posts + posts).distinct())
                }
            }
        }
    }
}