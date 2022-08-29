package com.example.foos.ui.view.screen.editprofile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.foos.R
import com.example.foos.ui.state.editprofile.EditProfileScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {

    private var _uiState = MutableStateFlow(EditProfileScreenUiState(
        "", "", ""
    ))

    val uiState = _uiState.asStateFlow()

    fun update(@StringRes key: Int, value: String) {
        when(key) {
            R.string.name -> _uiState.update { it.copy(username = value) }
            R.string.bio -> _uiState.update { it.copy(bio = value) }
        }
    }

    fun fetchUserData() {

    }
}