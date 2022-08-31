package com.example.foos.ui.view.screen.userprofile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.converter.uistate.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.GetPostsWithUserByUserIdWithDateUseCase
import com.example.foos.data.domain.converter.uistate.ConvertPostToUiStateUseCase
import com.example.foos.data.domain.fetcher.FetchPostsByUserIdUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ユーザプロフィール画面のViewModel
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchPostsByUserIdUseCase: FetchPostsByUserIdUseCase,
    private val convertPostToUiStateUseCase: ConvertPostToUiStateUseCase,
) : ViewModel() {

    private var _uiState = mutableStateOf(UserProfileScreenUiState.Default)
    val uiState: State<UserProfileScreenUiState> = _uiState

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
                _uiState.value = uiState.value.copy(
                    followerCount = follower.size,
                    followeeCount = followee.size,
                    following = !following
                )
            }
        }
    }

    /**
     * フォロワーリストのページへ遷移します
     */
    fun navigateToFollowerList(userId: String) {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.FollowList.route(userId, false.toString()))
        }
    }

    /**
     * フォロイーリストのページへ遷移します
     */
    fun navigateToFolloweeList(userId: String) {
        viewModelScope.launch {
            Log.d("NAV", SubScreen.FollowList.route(userId, true.toString()))
            _navEvent.emit(SubScreen.FollowList.route(userId, true.toString()))
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
        val data = uiState.postId
        viewModelScope.launch {
            _navEvent.emit("${SubScreen.PostDetail.route}/$data")
        }
    }

    /**
     * 投稿コンテンツの画像クリック時のイベント
     * @param imageUrls クリックされた画像を持つ投稿のUI状態
     * @param clickedImageUrl クリックされた画像のURL
     */
    fun onImageClick(imageUrls: List<String>, clickedImageUrl: String) {
        viewModelScope.launch {
            _navEvent.emit(
                SubScreen.ImageDetail.route(
                    imageUrls.map { Uri.encode(it) }.toString(),
                    clickedImageUrl
                )
            )
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
                _uiState.value = uiState.value.copy(
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

    private suspend fun fetchPosts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = fetchPostsByUserIdUseCase(userId).map {
                convertPostToUiStateUseCase(it)
            }
            _uiState.value = uiState.value.copy(posts = posts)
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
                val posts = fetchPostsByUserIdUseCase( uiState.value.userId, oldestDate ).map {
                    convertPostToUiStateUseCase(it)
                }
                _uiState.value =
                    uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }

    fun refreshUserPosts() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun fetchUserReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}