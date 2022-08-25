package com.example.foos.ui.view.screen.reaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.ConvertReactionToUiStateUseCase
import com.example.foos.data.domain.GetReactionsByUserIdUseCase
import com.example.foos.ui.state.screen.reaction.ReactionScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(
    private val getReactionsByUserIdUseCase: GetReactionsByUserIdUseCase,
    private val convertReactionToUiStateUseCase: ConvertReactionToUiStateUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(ReactionScreenUiState(listOf(), false))
    val uiState: StateFlow<ReactionScreenUiState> get() = _uiState

    fun fetchNewReactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            _uiState.value = _uiState.value.copy(
                reactions = getReactionsByUserIdUseCase.invoke(Firebase.auth.uid.toString())
                    .map { convertReactionToUiStateUseCase.invoke(it) })
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    fun onUserIconClick(userId: String) {

    }

    fun onContentClick() {

    }
}