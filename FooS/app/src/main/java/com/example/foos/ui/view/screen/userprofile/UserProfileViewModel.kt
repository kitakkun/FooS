package com.example.foos.ui.view.screen.userprofile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.converter.uistate.ConvertPostToUiStateUseCase
import com.example.foos.data.domain.fetcher.post.FetchPostsByUserIdUseCase
import com.example.foos.data.domain.fetcher.post.FetchPostsUserReactedByUserIdUseCase
import com.example.foos.data.domain.fetcher.post.FetchPostsWithMediaByUserIdUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.navigation.navargs.StringList
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
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
    private val fetchPostsUserReactedByUserIdUseCase: FetchPostsUserReactedByUserIdUseCase,
    private val fetchPostsWithMediaByUserIdUseCase: FetchPostsWithMediaByUserIdUseCase,
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

    fun onUserIconClick(userId: String) {
        viewModelScope.launch {
            if (userId != uiState.value.userId) {
                _navEvent.emit(SubScreen.UserProfile.route(userId))
            }
        }
    }

    /**
     * 投稿コンテンツクリック時のイベント
     * @param postId クリックされた投稿のUI状態
     */
    fun onContentClick(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostDetail.route(postId))
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
                    Uri.encode(Gson().toJson(StringList(imageUrls))),
                    imageUrls.indexOf(clickedImageUrl).toString(),
                )
            )
        }
    }

    /**
     * ユーザプロフィールをフェッチ
     */
    suspend fun fetchUserInfo(userId: String, onFinished: suspend() -> Unit = {}) {
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
            onFinished()
        }
    }

    /**
     * ユーザの古い投稿をフェッチします
     */
    fun fetchOlderUserPosts() {
        viewModelScope.launch {
            val oldestDate = uiState.value.posts.last().createdAt
            oldestDate?.let {
                val posts = fetchPostsByUserIdUseCase(uiState.value.userId, end = oldestDate).map {
                    convertPostToUiStateUseCase(it)
                }
                _uiState.value =
                    uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }

    /**
     * ユーザの新しい投稿をフェッチする
     */
    fun fetchNewUserPosts(indicateRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (indicateRefresh) {
                _uiState.value = uiState.value.copy(isRefreshing = true)
            }
            val posts = fetchPostsByUserIdUseCase(uiState.value.userId).map {
                convertPostToUiStateUseCase(it)
            }
            _uiState.value = uiState.value.copy(isRefreshing = false, posts = posts)
        }
    }

    /**
     * ユーザの新しいメディア付き投稿をフェッチする
     */
    fun fetchNewMediaPosts(indicateRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (indicateRefresh) {
                _uiState.value = uiState.value.copy(isRefreshing = true)
            }
            val posts = fetchPostsWithMediaByUserIdUseCase(uiState.value.userId).map {
                convertPostToUiStateUseCase(it)
            }
            _uiState.value = uiState.value.copy(isRefreshing = false, mediaPosts = posts)
        }
    }

    /**
     * ユーザの古いメディア付き投稿をフェッチする
     */
    fun fetchOlderMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt =
                uiState.value.mediaPosts.last().createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsWithMediaByUserIdUseCase(
                    uiState.value.userId,
                    end = lastCreatedAt
                ).map {
                    convertPostToUiStateUseCase(it)
                }
                _uiState.value = uiState.value.copy(
                    userReactedPosts = (uiState.value.mediaPosts + posts).distinct()
                )
            }
        }
    }

    /**
     * ユーザがリアクションした投稿リストを更新する
     */
    fun fetchNewUserReactedPosts(indicateRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (indicateRefresh) {
                _uiState.value = uiState.value.copy(isRefreshing = true)
            }
            val posts = fetchPostsUserReactedByUserIdUseCase(uiState.value.userId).map {
                convertPostToUiStateUseCase(it)
            }
            _uiState.value = uiState.value.copy(isRefreshing = false, userReactedPosts = posts)
        }
    }

    /**
     * より古いユーザがリアクションした投稿をフェッチする
     */
    fun fetchOlderUserReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt =
                uiState.value.userReactedPosts.last().reactions.find { it.from == uiState.value.userId }?.createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsUserReactedByUserIdUseCase(
                    uiState.value.userId,
                    end = lastCreatedAt
                ).map {
                    convertPostToUiStateUseCase(it)
                }
                _uiState.value = uiState.value.copy(
                    isRefreshing = false,
                    userReactedPosts = (uiState.value.userReactedPosts + posts).distinct()
                )
            }
        }
    }
}