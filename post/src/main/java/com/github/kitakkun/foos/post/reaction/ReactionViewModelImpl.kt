package com.github.kitakkun.foos.post.reaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.FetchReactionsByUserIdUseCase
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
class ReactionViewModelImpl @Inject constructor(
    private val fetchReactionsByUserIdUseCase: FetchReactionsByUserIdUseCase,
) : ViewModel(), ReactionViewModel {

    private var _uiState = MutableStateFlow(ReactionScreenUiState(listOf(), false))
    override val uiState: StateFlow<ReactionScreenUiState> = _uiState

    override fun fetchNewReactions(indicateRefreshing: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (indicateRefreshing) {
                _uiState.update { it.copy(isRefreshing = true) }
            }
            _uiState.update {
                it.copy(
                    reactions = fetchReactionsByUserIdUseCase(Firebase.auth.uid.toString())
                        .map { ReactionItemUiState.convert(it) }
                )
            }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    override fun onUserIconClick(userId: String) {

    }

    override fun onContentClick(reactionId: String) {

    }
}
