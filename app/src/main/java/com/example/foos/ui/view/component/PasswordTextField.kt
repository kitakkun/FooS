package com.example.foos.ui.view.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
import com.example.foos.R

@Composable
fun PasswordTextField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onVisibilityIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        label = { Text(text = stringResource(id = R.string.password)) },
        trailingIcon = {
            val icon = when (isPasswordVisible) {
                true -> Icons.Default.Visibility
                false -> Icons.Default.VisibilityOff
            }
            IconButton(onClick = onVisibilityIconClick) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        visualTransformation = when (isPasswordVisible) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun EmptyPasswordTextFieldPreview() {
    PasswordTextField(
        password = "",
        onPasswordChange = {},
        isPasswordVisible = true,
        onVisibilityIconClick = {},
    )
}

@Preview
@Composable
private fun VisiblePasswordTextFieldPreview() {
    PasswordTextField(
        password = "password",
        onPasswordChange = {},
        isPasswordVisible = true,
        onVisibilityIconClick = {},
    )
}

@Preview
@Composable
private fun InVisiblePasswordTextFieldPreview() {
    PasswordTextField(
        password = "password",
        onPasswordChange = {},
        isPasswordVisible = false,
        onVisibilityIconClick = {},
    )
}
