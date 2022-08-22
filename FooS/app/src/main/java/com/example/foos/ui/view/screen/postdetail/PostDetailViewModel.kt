package com.example.foos.ui.view.screen.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.GetPostWithUserByPostIdUseCase
import com.example.foos.data.model.PostWithUser
import com.example.foos.ui.state.screen.home.PostItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
) : ViewModel() {

    private var _uiState = MutableStateFlow(PostDetailScreenUiState(PostItemUiState.Default))
    val uiState: StateFlow<PostDetailScreenUiState> get() = _uiState

    fun setPostUiState(uiState: PostItemUiState) {
        this._uiState.update { it.copy(postItemUiState = uiState) }
    }

}