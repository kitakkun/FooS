package com.github.kitakkun.foos.post.postdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.model.DatabaseReaction
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.usecase.FetchPostByPostIdUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val fetchPostByPostIdUseCase: FetchPostByPostIdUseCase,
    private val reactionsRepository: ReactionsRepository
) : ViewModel() {

    private var _uiState = mutableStateOf(PostDetailScreenUiState(PostItemUiState.Default))
    val uiState: State<PostDetailScreenUiState> = _uiState

    suspend fun fetch(postId: String) {
        val state = fetchPostByPostIdUseCase(postId)?.let {
            PostItemUiState.convert(it)
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
            fetch(uiState.value.postItemUiState.postId)
        }
    }

    fun onReactionRemoved() {
        val id = uiState.value.postItemUiState.reactions.find {
            it.from == Firebase.auth.uid
        }?.reactionId

        id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                reactionsRepository.delete(it)
                fetch(uiState.value.postItemUiState.postId)
            }
        }
    }

}
