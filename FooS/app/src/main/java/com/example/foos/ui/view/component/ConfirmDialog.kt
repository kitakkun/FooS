package com.example.foos.ui.view.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * ユーザ操作の確認用アラートダイアログ
 * @param title ダイアログのタイトル
 * @param message ダイアログのメッセージ内容
 * @param confirmButtonText 確認ボタンのテキスト
 * @param dismissButtonText 拒否ボタンのテキスト
 * @param onConfirmed 確認ボタンが押下されたときの処理
 * @param onDismissed 拒否ボタンが押下された時の処理
 */
@Composable
fun ConfirmAlertDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmed: () -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        title = { Text(title) },
        text = { Text(text = message) },
        confirmButton = { TextButton(onClick = onConfirmed) { Text(confirmButtonText) } },
        dismissButton = { TextButton(onClick = onDismissed) { Text(dismissButtonText) } }
    )
}

@Preview
@Composable
private fun ConfirmAlertDialogPreview() {
    ConfirmAlertDialog(
        title = "Title",
        message = "some message",
        confirmButtonText = "OK",
        dismissButtonText = "Cancel",
    )
}