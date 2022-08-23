package com.example.foos.ui.view.screen.reaction

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foos.data.domain.GetReactionsByUserIdUseCase
import com.example.foos.ui.state.screen.reaction.ReactionScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(
    private val getReactionsByUserIdUseCase: GetReactionsByUserIdUseCase,
) : ViewModel() {

    private var _uiState = mutableStateOf(ReactionScreenUiState(listOf(), false))
    val uiState: State<ReactionScreenUiState> get() = _uiState

    suspend fun fetchReactions() {
        getReactionsByUserIdUseCase.invoke(Firebase.auth.uid.toString())
    }

    fun onUserIconClick(userId: String) {

    }

    fun onContentClick() {

    }
}