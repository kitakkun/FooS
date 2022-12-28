package com.example.foos.ui.view.screen.userprofile

import androidx.compose.runtime.State
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import kotlinx.coroutines.flow.SharedFlow

interface UserProfileViewModel {
    val uiState: State<UserProfileScreenUiState>
    val navEvent: SharedFlow<String>

    fun onFollowButtonClick()
    fun navigateToFollowerList(userId: String)
    fun navigateToFolloweeList(userId: String)
    fun onUserIconClick(userId: String)
    fun onContentClick(postId: String)
    fun onImageClick(imageUrls: List<String>, clickedImageUrl: String)
    fun fetchInitialPosts()
    fun refreshPosts()
    fun fetchInitialMediaPosts()
    fun refreshMediaPosts()
    fun fetchInitialReactedPosts()
    fun refreshReactedPosts()
    suspend fun fetchUserInfo(userId: String, onFinished: suspend () -> Unit = {})
    fun fetchOlderPosts()
    fun fetchOlderMediaPosts()
    fun fetchOlderUserReactedPosts()
    fun onMoreVertClick(postId: String)
}