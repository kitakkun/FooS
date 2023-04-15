package com.github.kitakkun.foos.ui.view.screen.home

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.SharedFlow

interface HomeViewModel {
    val uiState: State<HomeScreenUiState>
    val navEvent: SharedFlow<String>
    fun onPostCreateButtonClick()
    fun onUserIconClick(userId: String)
    fun onContentClick(postId: String)
    fun onImageClick(imageUrls: List<String>, clickedImageUrl: String)
    fun onRefresh()
    fun fetchInitialPosts()
    fun fetchOlderPosts()
    fun onMoreVertClick(postId: String)
}
