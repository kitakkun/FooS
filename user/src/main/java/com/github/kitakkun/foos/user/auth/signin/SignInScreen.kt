package com.github.kitakkun.foos.user.auth.signin

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
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
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.composable.loading.BoxWithLoading
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.LinkBlue
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.auth.EmailTextField
import com.github.kitakkun.foos.user.auth.PasswordTextField

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navController: NavController,
) {
    SignInUI(
        uiState = viewModel.uiState.value,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onPasswordVisibilityIconClick = viewModel::togglePasswordVisibility,
        onSignInClick = {
            viewModel.signIn { success ->
                if (success) {
                    navController.navigateToSingleScreen(ScreenRouter.Main)
                }
            }
        },
        onSignUpClick = {
            navController.navigateToSingleScreen(UserScreenRouter.Auth.SignUp)
        },
    )
}

@Composable
fun SignInUI(
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityIconClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val signUpText = stringResource(id = R.string.sign_up)
    val haveAccountText = stringResource(id = R.string.dont_have_account)
    val haveAccountTextColor = MaterialTheme.colors.onBackground
    val signUpNavigateText = remember {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = haveAccountTextColor)) {
                append(haveAccountText)
            }
            append(" ")
            // make SignUp text clickable by using annotation.
            pushStringAnnotation(tag = "SignUp", annotation = signUpText)
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = LinkBlue,
                )
            ) {
                append(signUpText)
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
            modifier= Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h4)
            EmailTextField(
                email = uiState.email,
                onEmailChange = onEmailChange,
                modifier = Modifier.widthIn(max = 300.dp)
            )
            PasswordTextField(
                password = uiState.password,
                isPasswordVisible = uiState.isPasswordVisible,
                onPasswordChange = onPasswordChange,
                onVisibilityIconClick = onPasswordVisibilityIconClick,
                modifier = Modifier.widthIn(max = 300.dp)
            )
            Button(onClick = onSignInClick) {
                Text(text = stringResource(id = R.string.sign_in))
            }
            ClickableText(
                text = signUpNavigateText,
                onClick = { offset ->
                    signUpNavigateText.getStringAnnotations(
                        tag = "SignUp",
                        start = offset,
                        end = offset
                    ).firstOrNull() ?: return@ClickableText
                    onSignUpClick()
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignInScreenPreview() = PreviewContainer {
    SignInUI(
        uiState = SignInUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onPasswordVisibilityIconClick = {},
    )
}
