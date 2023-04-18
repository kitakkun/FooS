package com.github.kitakkun.foos.user.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.kitakkun.foos.customview.composable.loading.BoxWithLoading
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.LinkBlue
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.auth.EmailTextField
import com.github.kitakkun.foos.user.auth.PasswordTextField
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        launch {
            viewModel.navEvent.collect {
                navController.navigate(it) {
                    launchSingleTop = true
                }
            }
        }
    }

    SignUpUI(
        uiState = viewModel.uiState.value,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onSignInClick = viewModel::navigateToSignIn,
        onSignUpClick = viewModel::signUp,
        onPasswordVisibilityIconClick = viewModel::togglePasswordVisibility,
    )
}

@Composable
fun SignUpUI(
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityIconClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val signInText = stringResource(id = R.string.sign_in)
    val haveAccountText = stringResource(id = R.string.already_have_account)
    val signUpNavigateText = remember {
        buildAnnotatedString {
            append(haveAccountText)
            append(" ")
            // make SignUp text clickable by using annotation.
            pushStringAnnotation(tag = "SignIn", annotation = signInText)
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = LinkBlue,
                )
            ) {
                append(signInText)
            }
            pop()
        }
    }
    BoxWithLoading(
        isLoading = uiState.isLoading,
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h4)
            EmailTextField(
                email = uiState.email,
                onEmailChange = onEmailChange
            )
            PasswordTextField(
                isPasswordVisible = uiState.isPasswordVisible,
                password = uiState.password,
                onPasswordChange = onPasswordChange,
                onVisibilityIconClick = onPasswordVisibilityIconClick,
            )
            Button(onClick = onSignUpClick) {
                Text(text = stringResource(id = R.string.sign_up))
            }
            ClickableText(
                text = signUpNavigateText,
                onClick = { offset ->
                    signUpNavigateText.getStringAnnotations(
                        tag = "SignIn",
                        start = offset,
                        end = offset
                    ).firstOrNull() ?: return@ClickableText
                    onSignInClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun SignUpUIPreview() = PreviewContainer {
    SignUpUI(
        uiState = SignUpUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onPasswordVisibilityIconClick = {},
    )
}
