package com.example.foos.ui.view.component.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            modifier = modifier,
        ) {
            content()
        }
    } else {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            modifier = modifier,
        ) {
            content()
        }
    }
}