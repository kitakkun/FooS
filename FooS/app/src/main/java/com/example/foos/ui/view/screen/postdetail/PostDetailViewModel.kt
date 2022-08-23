package com.example.foos.ui.view.screen.postdetail

import androidx.lifecycle.ViewModel
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.postdetail.PostDetailScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
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
    fun onReactionButtonClicked() {
        /* TODO: リアクションの追加・削除処理 */
    }

}