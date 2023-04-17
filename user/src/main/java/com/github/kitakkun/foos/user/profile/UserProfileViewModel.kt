package com.github.kitakkun.foos.user.profile

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.SharedFlow

interface UserProfileViewModel {
    val uiState: State<UserProfileScreenUiState>
    val navEvent: SharedFlow<String>

    fun toggleFollowState()
    fun navigateToFollowerUsersList(userId: String)
    fun navigateToFollowingUsersList(userId: String)
    fun navigateToUserProfile(userId: String)
    fun navigateToPostDetail(postId: String)
    fun openImageDetailView(imageUrls: List<String>, clickedImageUrl: String)
    fun fetchInitialPosts()
    fun refreshPosts()
    fun fetchInitialMediaPosts()
    fun refreshMediaPosts()
    fun fetchInitialReactedPosts()
    fun refreshReactedPosts()
    suspend fun fetchProfileInfo(userId: String, onFinished: suspend () -> Unit = {})
    fun fetchOlderPosts()
    fun fetchOlderMediaPosts()
    fun fetchOlderReactedPosts()
    fun showPostOptions(postId: String)
}
