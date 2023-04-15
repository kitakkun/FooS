package com.github.kitakkun.foos.post.reaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.FetchReactionsByUserIdUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReactionViewModelImpl @Inject constructor(
    private val fetchReactionsByUserIdUseCase: FetchReactionsByUserIdUseCase,
) : ViewModel(), ReactionViewModel {

    private var _uiState = mutableStateOf(ReactionScreenUiState(listOf(), false))
    override val uiState: State<ReactionScreenUiState> = _uiState

    override fun fetchNewReactions(indicateRefreshing: Boolean) {
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

    override fun onUserIconClick(userId: String) {

    }

    override fun onContentClick(reactionId: String) {

    }
}
