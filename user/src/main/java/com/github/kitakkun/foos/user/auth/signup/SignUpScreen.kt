package com.github.kitakkun.foos.user.auth.signup

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.github.kitakkun.foos.common.ext.navigateToSingleScreen
import com.github.kitakkun.foos.common.model.Password
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.composable.loading.BoxWithLoading
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.LinkBlue
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.auth.EmailError
import com.github.kitakkun.foos.user.auth.EmailTextField
import com.github.kitakkun.foos.user.auth.PasswordError
import com.github.kitakkun.foos.user.auth.PasswordTextField

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController,
) {
    SignUpUI(
        uiState = viewModel.uiState.value,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onSignInClick = {
            navController.navigateToSingleScreen(UserScreenRouter.Auth.SignIn)
        },
        onSignUpClick = {
            viewModel.signUp { success ->
                if (success) {
                    navController.navigateToSingleScreen(ScreenRouter.Main)
                }
            }
        },
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
    val haveAccountTextColor = MaterialTheme.colors.onBackground
    val signUpNavigateText = remember {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = haveAccountTextColor)) {
                append(haveAccountText)
            }
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

    val passwordErrorMessage = when (uiState.passwordError) {
        PasswordError.BLANK -> stringResource(id = R.string.blank_password_error_msg)
        PasswordError.TOO_LONG -> stringResource(
            id = R.string.too_long_password_error_msg,
            Password.MAX_LENGTH
        )
        PasswordError.TOO_SHORT -> stringResource(
            id = R.string.too_short_password_error_msg,
            Password.MIN_LENGTH
        )
        PasswordError.NO_NUMERIC_CHARACTER -> stringResource(id = R.string.no_numeric_character_password_error_msg)
        PasswordError.NO_ALPHABETIC_CHARACTER -> stringResource(id = R.string.no_alphabetic_character_password_error_msg)
        PasswordError.CONTAIN_INVALID_CHARACTER -> stringResource(id = R.string.contain_invalid_character_password_error_msg)
        else -> null
    }

    val emailErrorMessage = when (uiState.emailError) {
        EmailError.INVALID_FORMAT -> stringResource(id = R.string.invalid_email_error_msg)
        EmailError.BLANK -> stringResource(id = R.string.blank_email_error_msg)
        else -> null
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
                onEmailChange = onEmailChange,
                isError = uiState.isEmailErrorVisible,
                errorMessage = emailErrorMessage,
            )
            PasswordTextField(
                isPasswordVisible = uiState.isPasswordVisible,
                password = uiState.password,
                onPasswordChange = onPasswordChange,
                onVisibilityIconClick = onPasswordVisibilityIconClick,
                isError = uiState.isPasswordErrorVisible,
                errorMessage = passwordErrorMessage
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
