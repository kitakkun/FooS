package com.github.kitakkun.foos.user.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.navigation.BottomSheet
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchPostsByUserIdUseCase
import com.github.kitakkun.foos.common.usecase.FetchPostsUserReactedByUserIdUseCase
import com.github.kitakkun.foos.common.usecase.FetchPostsWithMediaByUserIdUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ユーザプロフィール画面のViewModel
 */
@HiltViewModel
class UserProfileViewModelImpl @Inject constructor(
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchPostsByUserIdUseCase: FetchPostsByUserIdUseCase,
    private val fetchPostsUserReactedByUserIdUseCase: FetchPostsUserReactedByUserIdUseCase,
    private val fetchPostsWithMediaByUserIdUseCase: FetchPostsWithMediaByUserIdUseCase,
) : ViewModel(), UserProfileViewModel {

    private var _uiState = mutableStateOf(UserProfileScreenUiState())
    override val uiState: State<UserProfileScreenUiState> = _uiState

    // ナビゲーションイベント
    private var _navEvent = MutableSharedFlow<String>()
    override val navEvent: SharedFlow<String> get() = _navEvent

    override fun toggleFollowState() {
        viewModelScope.launch(Dispatchers.IO) {
            val following = uiState.value.isFollowedByClientUser
            val clientUserId = Firebase.auth.uid ?: return@launch
            val profileUserId = uiState.value.id
            when (following) {
                true -> followRepository.deleteFollowGraph(
                    from = clientUserId,
                    to = profileUserId,
                )
                false -> followRepository.createFollowGraph(
                    from = clientUserId,
                    to = profileUserId,
                )
            }
            val followCount = followRepository.fetchFollowingCount(userId = uiState.value.id)
            val followerCount = followRepository.fetchFollowerCount(uiState.value.id)
            _uiState.value = uiState.value.copy(
                followerCount = followerCount,
                followCount = followCount,
                isFollowedByClientUser = !following
            )
        }
    }

    override fun navigateToFollowerUsersList(userId: String) {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.FollowList.route(userId, false.toString()))
        }
    }

    override fun navigateToFollowingUsersList(userId: String) {
        viewModelScope.launch {
            Log.d("NAV", SubScreen.FollowList.route(userId, true.toString()))
            _navEvent.emit(SubScreen.FollowList.route(userId, true.toString()))
        }
    }

    override fun navigateToUserProfile(userId: String) {
        viewModelScope.launch {
            if (userId != uiState.value.id) {
                _navEvent.emit(SubScreen.UserProfile.route(userId))
            }
        }
    }

    override fun navigateToPostDetail(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostDetail.route(postId))
        }
    }

    override fun openImageDetailView(imageUrls: List<String>, clickedImageUrl: String) {
        viewModelScope.launch {
            _navEvent.emit(
                SubScreen.ImageDetail.route(
                    Uri.encode(Gson().toJson(StringList(imageUrls))),
                    imageUrls.indexOf(clickedImageUrl).toString(),
                )
            )
        }
    }

    override fun fetchInitialPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoadingPosts = true)
            val posts =
                fetchPostsByUserIdUseCase(uiState.value.id).map { PostItemUiState.convert(it) }
            _uiState.value = uiState.value.copy(isLoadingPosts = false, posts = posts)
        }
    }

    override fun refreshPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts =
                fetchPostsByUserIdUseCase(uiState.value.id).map { PostItemUiState.convert(it) }
            _uiState.value = uiState.value.copy(isRefreshing = false, posts = posts)
        }
    }

    override fun fetchInitialMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoadingMediaPosts = true)
            val posts = fetchPostsWithMediaByUserIdUseCase(uiState.value.id).map {
                PostItemUiState.convert(it)
            }
            _uiState.value = uiState.value.copy(isLoadingMediaPosts = false, mediaPosts = posts)
        }
    }

    override fun refreshMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsWithMediaByUserIdUseCase(uiState.value.id).map {
                PostItemUiState.convert(it)
            }
            _uiState.value = uiState.value.copy(isRefreshing = false, mediaPosts = posts)
        }
    }

    override fun fetchInitialReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoadingUserReactedPosts = true)
            val posts = fetchPostsUserReactedByUserIdUseCase(uiState.value.id).map {
                PostItemUiState.convert(it)
            }
            _uiState.value =
                uiState.value.copy(isLoadingUserReactedPosts = false, reactedPosts = posts)
        }
    }

    override fun refreshReactedPosts() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsUserReactedByUserIdUseCase(uiState.value.id).map {
                PostItemUiState.convert(it)
            }
            _uiState.value = uiState.value.copy(isRefreshing = false, reactedPosts = posts)
        }
    }

    override suspend fun fetchProfileInfo(userId: String, onFinished: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val followers = followRepository.fetchByFolloweeId(userId)
            val followees = followRepository.fetchByFollowerId(userId)
            val user = usersRepository.fetchByUserId(userId)
            user?.let {
                _uiState.value = uiState.value.copy(
                    id = user.id,
                    profileImageUrl = user.profileImage,
                    name = user.name,
                    followCount = followees.size,
                    followerCount = followers.size,
                    isFollowedByClientUser = followers.map { followInfo -> followInfo.from }
                        .contains(Firebase.auth.uid.toString())
                )
            }
            onFinished()
        }
    }

    override fun fetchOlderPosts() {
        viewModelScope.launch {
            val oldestDate = uiState.value.posts.last().createdAt
            oldestDate?.let {
                val posts = fetchPostsByUserIdUseCase(uiState.value.id, end = oldestDate).map {
                    PostItemUiState.convert(it)
                }
                _uiState.value =
                    uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }

    override fun fetchOlderMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt =
                uiState.value.mediaPosts.last().createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsWithMediaByUserIdUseCase(
                    uiState.value.id,
                    end = lastCreatedAt
                ).map {
                    PostItemUiState.convert(it)
                }
                _uiState.value = uiState.value.copy(
                    reactedPosts = (uiState.value.mediaPosts + posts).distinct()
                )
            }
        }
    }

    override fun fetchOlderReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt =
                uiState.value.reactedPosts.last().reactions.find { it.from == uiState.value.id }?.createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsUserReactedByUserIdUseCase(
                    uiState.value.id,
                    end = lastCreatedAt
                ).map {
                    PostItemUiState.convert(it)
                }
                _uiState.value = uiState.value.copy(
                    isRefreshing = false,
                    reactedPosts = (uiState.value.reactedPosts + posts).distinct()
                )
            }
        }
    }

    override fun showPostOptions(postId: String) {
        viewModelScope.launch {
            _navEvent.emit(BottomSheet.PostOption.route(postId))
        }
    }
}
