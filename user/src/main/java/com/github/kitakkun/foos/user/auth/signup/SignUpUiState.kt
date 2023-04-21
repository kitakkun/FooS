package com.github.kitakkun.foos.user.auth.signup

import com.github.kitakkun.foos.user.auth.EmailError
import com.github.kitakkun.foos.user.auth.PasswordError

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isEmailErrorVisible: Boolean = false,
    val isPasswordErrorVisible: Boolean = false,
    val emailError: EmailError? = null,
    val passwordError: PasswordError? = null,
)
