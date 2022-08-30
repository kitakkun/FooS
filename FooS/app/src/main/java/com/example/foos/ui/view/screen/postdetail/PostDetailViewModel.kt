package com.example.foos.ui.view.screen.postdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.GetPostWithUserByPostIdUseCase
import com.example.foos.data.model.DatabaseReaction
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.postdetail.PostDetailScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostWithUserByPostIdUseCase: GetPostWithUserByPostIdUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase,
    private val reactionsRepository: ReactionsRepository
) : ViewModel() {

    private var _uiState = mutableStateOf(PostDetailScreenUiState(PostItemUiState.Default))
    val uiState: State<PostDetailScreenUiState> = _uiState

    fun setPostUiState(uiState: PostItemUiState) {
        _uiState.value = _uiState.value.copy(postItemUiState = uiState)
    }

    suspend fun fetchPost(postId: String) {
        val state = getPostWithUserByPostIdUseCase(postId)?.let {
            convertPostWithUserToUiStateUseCase(it)
        }
        state?.let {
            _uiState.value = uiState.value.copy(postItemUiState = state)
        }
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