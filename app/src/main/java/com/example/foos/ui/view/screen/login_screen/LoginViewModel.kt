package com.example.foos.ui.view.screen.login_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var auth: FirebaseAuth

    private val mutableUiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> = mutableUiState

    fun onEmailChange(value: String) {
        mutableUiState.value = mutableUiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        mutableUiState.value = mutableUiState.value.copy(password = value)
    }

    fun onPasswordVisibilityChange(value: Boolean) {
        mutableUiState.value = mutableUiState.value.copy(isPasswordVisible = value)
    }

    fun onSignInButtonClick() {

    }
}
