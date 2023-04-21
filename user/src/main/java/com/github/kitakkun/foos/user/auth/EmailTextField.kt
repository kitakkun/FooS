package com.github.kitakkun.foos.user.auth

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.R

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    TextFieldWithError(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(id = R.string.email),
        placeholder = stringResource(id = R.string.example_email),
        trailingIcon = Icons.Default.Email,
        isError = isError,
        errorMessage = errorMessage,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )
}

@Preview
@Composable
private fun EmptyEmailTextFieldPreview() = PreviewContainer {
    EmailTextField(
        email = "",
        onEmailChange = {},
    )
}

@Preview
@Composable
private fun NonEmptyEmailTextFieldPreview() = PreviewContainer {
    EmailTextField(
        email = "example@example.com",
        onEmailChange = {},
    )
}

@Preview
@Composable
private fun ErrorEmailTextFieldPreview() = PreviewContainer {
    EmailTextField(
        email = "hogehoge",
        onEmailChange = {},
        isError = true,
        errorMessage = "error",
    )
}
