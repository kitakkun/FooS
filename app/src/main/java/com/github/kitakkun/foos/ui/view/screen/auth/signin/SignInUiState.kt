package com.github.kitakkun.foos.ui.view.screen.auth.signin

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
)
