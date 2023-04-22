package com.github.kitakkun.foos.post.reaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.FetchReactionsByUserIdUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReactionViewModel(
    private val fetchReactionsByUserIdUseCase: FetchReactionsByUserIdUseCase,
) : ViewModel() {

    private var _uiState = mutableStateOf(ReactionScreenUiState(listOf(), false))
    val uiState: State<ReactionScreenUiState> = _uiState

    fun fetchNewReactions(indicateRefreshing: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (indicateRefreshing) {
                _uiState.value = uiState.value.copy(isRefreshing = true)
            }
            _uiState.value = uiState.value.copy(
                reactions = fetchReactionsByUserIdUseCase.invoke(Firebase.auth.uid.toString())
                    .map { ReactionItemUiState.convert(it) })
            _uiState.value = uiState.value.copy(isRefreshing = false)
        }
    }

    fun onUserIconClick(userId: String) {

    }

    fun onContentClick(reactionId: String) {

    }
}
