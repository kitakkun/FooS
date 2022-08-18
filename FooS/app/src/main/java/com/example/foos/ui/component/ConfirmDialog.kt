package com.example.foos.ui.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmAlertDialog(
    title: String = "Title",
    message: String = "message",
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onConfirmed: () -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = { onDismissed.invoke() },
        title = { Text(title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = {
                onConfirmed.invoke()
            }) { Text(confirmButtonText) }
        },
        dismissButton = {
            TextButton(onClick = { onDismissed.invoke() }) {
                Text(dismissButtonText)
            }
        }
    )
}