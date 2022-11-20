package com.example.foos.ui.view.screen.login_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginUi(
        uiState = uiState,
        onCreateAccountClick = viewModel::onCreateAccountClick,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onLoginTextClick = viewModel::onLoginTextClick,
    )
}
