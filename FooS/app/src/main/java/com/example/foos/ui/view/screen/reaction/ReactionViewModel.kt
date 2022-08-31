package com.example.foos.ui.view.screen.reaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.FetchReactionsByUserIdUseCase
import com.example.foos.data.domain.converter.uistate.ConvertReactionToUiStateUseCase
import com.example.foos.ui.state.screen.reaction.ReactionScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(
    private val fetchReactionsByUserIdUseCase: FetchReactionsByUserIdUseCase,
    private val convertReactionToUiStateUseCase: ConvertReactionToUiStateUseCase,
) : ViewModel() {

    private var _uiState = mutableStateOf(ReactionScreenUiState(listOf(), false))
    val uiState: State<ReactionScreenUiState> = _uiState

    fun fetchNewReactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isRefreshing = true)
            _uiState.value = uiState.value.copy(
                reactions = fetchReactionsByUserIdUseCase.invoke(Firebase.auth.uid.toString())
                    .map { convertReactionToUiStateUseCase.invoke(it) })
            _uiState.value = uiState.value.copy(isRefreshing = false)
        }
    }

    fun onUserIconClick(userId: String) {

    }

    fun onContentClick() {

    }
}