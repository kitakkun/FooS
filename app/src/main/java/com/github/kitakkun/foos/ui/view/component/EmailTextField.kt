package com.github.kitakkun.foos.ui.view.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.R
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        label = { Text(text = stringResource(id = R.string.email)) },
        placeholder = { Text(text = stringResource(id = R.string.example_email)) },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        },
        modifier = modifier,
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
