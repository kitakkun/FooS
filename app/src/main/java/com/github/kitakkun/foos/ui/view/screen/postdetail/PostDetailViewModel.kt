package com.github.kitakkun.foos.ui.view.screen.postdetail

import androidx.compose.runtime.State

interface PostDetailViewModel {
    val uiState: State<PostDetailScreenUiState>
    suspend fun fetch(postId: String)
    fun onUserInfoClicked()
    fun onGoogleMapsClicked()
    fun onReactionButtonClicked(reactionString: String)
    fun onReactionRemoved()
}
