package com.github.kitakkun.foos.user.auth.signup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
)
