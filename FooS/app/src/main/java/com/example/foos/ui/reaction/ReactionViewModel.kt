package com.example.foos.ui.reaction

import android.view.View
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(): ViewModel() {

    private var _uiState = mutableStateOf(ReactionScreenUiState(listOf()))
    val uiState: State<ReactionScreenUiState> get() = _uiState
}