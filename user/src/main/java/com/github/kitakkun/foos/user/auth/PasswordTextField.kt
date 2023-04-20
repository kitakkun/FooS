package com.github.kitakkun.foos.user.auth

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.R

@Composable
fun PasswordTextField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onVisibilityIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    TextFieldWithError(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(id = R.string.password),
        trailingIcon = when (isPasswordVisible) {
            true -> Icons.Default.Visibility
            false -> Icons.Default.VisibilityOff
        },
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        visualTransformation = when (isPasswordVisible) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        onClickTrailingIcon = onVisibilityIconClick,
    )
}

@Preview
@Composable
private fun EmptyPasswordTextFieldPreview() = PreviewContainer {
    PasswordTextField(
        password = "",
        onPasswordChange = {},
        isPasswordVisible = true,
        onVisibilityIconClick = {},
    )
}

@Preview
@Composable
private fun VisiblePasswordTextFieldPreview() = PreviewContainer {
    PasswordTextField(
        password = "password",
        onPasswordChange = {},
        isPasswordVisible = true,
        onVisibilityIconClick = {},
    )
}

@Preview
@Composable
private fun InVisiblePasswordTextFieldPreview() = PreviewContainer {
    PasswordTextField(
        password = "password",
        onPasswordChange = {},
        isPasswordVisible = false,
        onVisibilityIconClick = {},
    )
}
