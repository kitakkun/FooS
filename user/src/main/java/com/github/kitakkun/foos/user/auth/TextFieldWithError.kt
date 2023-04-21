package com.github.kitakkun.foos.user.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun TextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: ImageVector,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClickTrailingIcon: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = placeholder ?: "")
            },
            label = {
                Text(text = label)
            },
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            trailingIcon = {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = onClickTrailingIcon ?: {},
                        enabled = onClickTrailingIcon != null,
                    )
                )
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(16.dp),
            visualTransformation = visualTransformation,
        )
        Text(
            text = errorMessage ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview
@Composable
private fun TextFieldWithErrorPreview() = PreviewContainer {
    TextFieldWithError(
        value = "",
        label = "Email",
        placeholder = "",
        trailingIcon = Icons.Default.Email,
        onValueChange = {},
        isError = true,
        errorMessage = "Invalid email address",
    )
}
