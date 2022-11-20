package com.example.foos.ui.view.screen.login_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginUi(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onCreateAccountClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(text = "Welcome to FooS!", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = uiState.email,
            singleLine = true,
            onValueChange = onEmailChange,
            label = { Text(text = "email") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            TextField(
                value = uiState.password,
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                onValueChange = onPasswordChange,
                label = { Text(text = "password") },
                trailingIcon = {
                    Icon(
                        imageVector = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable { onPasswordVisibilityChange.invoke(!uiState.isPasswordVisible) }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onCreateAccountClick) {
            Text(text = "Create account")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Have an account?")
            ClickableText(text = buildAnnotatedString {
                append("Log in")
            }, onClick = {})
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginUiPreview() {
    LoginUi(
        uiState = LoginUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onPasswordVisibilityChange = {}
    ) {
    }
}
