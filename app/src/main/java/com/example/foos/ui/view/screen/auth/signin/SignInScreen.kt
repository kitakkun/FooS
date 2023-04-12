package com.example.foos.ui.view.screen.auth.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
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
import com.example.foos.R
import com.example.foos.ui.theme.LinkBlue

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val signUpText = stringResource(id = R.string.sign_up)
    val haveAccountText = stringResource(id = R.string.dont_have_account)
    val signUpNavigateText = remember {
        buildAnnotatedString {
            append(haveAccountText)
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h4)
            TextField(value = uiState.email, onValueChange = onEmailChange)
            TextField(value = uiState.password, onValueChange = onPasswordChange)
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
@Composable
private fun AuthScreenPreview() {
    SignInScreen(
        uiState = SignInUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
    )
}
