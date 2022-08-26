package com.example.foos.ui.view.screen.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.model.DatabaseReaction
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.postdetail.PostDetailScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val reactionsRepository: ReactionsRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(PostDetailScreenUiState(PostItemUiState.Default))
    val uiState: StateFlow<PostDetailScreenUiState> get() = _uiState

    fun setPostUiState(uiState: PostItemUiState) {
        this._uiState.update { it.copy(postItemUiState = uiState) }
    }

    /**
     * ユーザー情報がクリックされたときの処理
     */
    fun onUserInfoClicked() {
        /* TODO: ユーザプロフィールへ遷移 */
    }

    /**
     * Google Mapsがクリックされたときの処理
     */
    fun onGoogleMapsClicked() {
        /* TODO: マップを開く */
    }

    /**
     * リアクションボタンが押されたときの処理
     */
    fun onReactionButtonClicked(reactionString: String) {
        val reaction = DatabaseReaction(
            reactionId = "",
            from = Firebase.auth.uid.toString(),
            to = uiState.value.postItemUiState.userId,
            postId = uiState.value.postItemUiState.postId,
            reaction = reactionString,
        )
        viewModelScope.launch(Dispatchers.IO) {
            reactionsRepository.create(reaction)
        }
    }

}