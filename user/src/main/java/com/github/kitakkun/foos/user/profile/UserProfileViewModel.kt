package com.github.kitakkun.foos.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.repository.FollowRepository
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.common.usecase.FetchPostsByUserIdUseCase
import com.github.kitakkun.foos.common.usecase.FetchPostsUserReactedByUserIdUseCase
import com.github.kitakkun.foos.common.usecase.FetchPostsWithMediaByUserIdUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userId: String,
    private val usersRepository: UsersRepository,
    private val followRepository: FollowRepository,
    private val fetchPostsByUserIdUseCase: FetchPostsByUserIdUseCase,
    private val fetchPostsUserReactedByUserIdUseCase: FetchPostsUserReactedByUserIdUseCase,
    private val fetchPostsWithMediaByUserIdUseCase: FetchPostsWithMediaByUserIdUseCase,
) : ViewModel() {

    private var mutableUiState = MutableStateFlow(UserProfileScreenUiState())
    val uiState = mutableUiState.asStateFlow()

    fun toggleFollowState() {
        viewModelScope.launch(Dispatchers.IO) {
            val following = uiState.value.isFollowedByClientUser
            val clientUserId = Firebase.auth.uid ?: return@launch
            when (following) {
                true -> followRepository.deleteFollowGraph(
                    from = clientUserId,
                    to = userId,
                )

                false -> followRepository.createFollowGraph(
                    from = clientUserId,
                    to = userId,
                )
            }
            val followCount = followRepository.fetchFollowingCount(userId = userId)
            val followerCount = followRepository.fetchFollowerCount(userId = userId)
            mutableUiState.value = uiState.value.copy(
                followerCount = followerCount.toInt(),
                followCount = followCount.toInt(),
                isFollowedByClientUser = !following
            )
        }
    }

    fun fetchInitialPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value = uiState.value.copy(isLoadingPosts = true)
            val posts = fetchPostsByUserIdUseCase(userId).map { PostItemUiState.convert(it) }
            mutableUiState.value = uiState.value.copy(isLoadingPosts = false, posts = posts)
        }
    }

    fun refreshPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsByUserIdUseCase(userId).map { PostItemUiState.convert(it) }
            mutableUiState.value = uiState.value.copy(isRefreshing = false, posts = posts)
        }
    }

    fun fetchInitialMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value = uiState.value.copy(isLoadingMediaPosts = true)
            val posts = fetchPostsWithMediaByUserIdUseCase(userId).map {
                PostItemUiState.convert(it)
            }
            mutableUiState.value =
                uiState.value.copy(isLoadingMediaPosts = false, mediaPosts = posts)
        }
    }

    fun refreshMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsWithMediaByUserIdUseCase(userId).map {
                PostItemUiState.convert(it)
            }
            mutableUiState.value = uiState.value.copy(isRefreshing = false, mediaPosts = posts)
        }
    }

    fun fetchInitialReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.value = uiState.value.copy(isLoadingUserReactedPosts = true)
            val posts = fetchPostsUserReactedByUserIdUseCase(userId).map {
                PostItemUiState.convert(it)
            }
            mutableUiState.value =
                uiState.value.copy(isLoadingUserReactedPosts = false, reactedPosts = posts)
        }
    }

    fun refreshReactedPosts() {
        viewModelScope.launch {
            mutableUiState.value = uiState.value.copy(isRefreshing = true)
            val posts = fetchPostsUserReactedByUserIdUseCase(userId).map {
                PostItemUiState.convert(it)
            }
            mutableUiState.value = uiState.value.copy(isRefreshing = false, reactedPosts = posts)
        }
    }

    suspend fun fetchProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val clientUserId = Firebase.auth.uid ?: return@launch
            val user = usersRepository.fetchByUserId(userId) ?: return@launch
            val followerCount = followRepository.fetchFollowingCount(userId)
            val followingCount = followRepository.fetchFollowerCount(userId)
            val isFollowedByClientUser = followRepository.fetchFollowGraph(
                from = clientUserId,
                to = userId
            ) != null
            mutableUiState.value = uiState.value.copy(
                id = user.id,
                profileImageUrl = user.profileImage,
                name = user.name,
                followCount = followingCount.toInt(),
                followerCount = followerCount.toInt(),
                isFollowedByClientUser = isFollowedByClientUser,
            )
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
            val oldestDate = uiState.value.posts.last().createdAt
            oldestDate?.let {
                val posts = fetchPostsByUserIdUseCase(userId, end = oldestDate).map {
                    PostItemUiState.convert(it)
                }
                mutableUiState.value =
                    uiState.value.copy(posts = (uiState.value.posts + posts).distinct())
            }
        }
    }

    fun fetchOlderMediaPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt = uiState.value.mediaPosts.last().createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsWithMediaByUserIdUseCase(
                    userId, end = lastCreatedAt
                ).map {
                    PostItemUiState.convert(it)
                }
                mutableUiState.value = uiState.value.copy(
                    reactedPosts = (uiState.value.mediaPosts + posts).distinct()
                )
            }
        }
    }

    fun fetchOlderReactedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCreatedAt =
                uiState.value.reactedPosts.last().reactions.find { it.from == userId }?.createdAt
            lastCreatedAt?.let {
                val posts = fetchPostsUserReactedByUserIdUseCase(
                    userId, end = lastCreatedAt
                ).map {
                    PostItemUiState.convert(it)
                }
                mutableUiState.value = uiState.value.copy(
                    isRefreshing = false,
                    reactedPosts = (uiState.value.reactedPosts + posts).distinct()
                )
            }
        }
    }
}
