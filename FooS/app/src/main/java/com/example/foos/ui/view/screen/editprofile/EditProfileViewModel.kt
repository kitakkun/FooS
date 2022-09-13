package com.example.foos.ui.view.screen.editprofile

import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foos.R
import com.example.foos.ui.state.editprofile.EditProfileScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = mutableStateOf(EditProfileScreenUiState.Default)

    val uiState: State<EditProfileScreenUiState> = _uiState

    fun update(@StringRes key: Int, value: String) {
        when (key) {
            R.string.name -> _uiState.value = uiState.value.copy(username = value)
            R.string.bio -> _uiState.value = uiState.value.copy(bio = value)
        }
    }
}
