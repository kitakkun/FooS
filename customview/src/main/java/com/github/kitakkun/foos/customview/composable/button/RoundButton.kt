package com.github.kitakkun.foos.customview.composable.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.FooSTheme

@Composable
fun RoundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outlined: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (outlined) {
        OutlinedButton(
            enabled = enabled,
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

@Preview
@Composable
private fun RoundButtonPreview() = PreviewContainer {
    FooSTheme {
        RoundButton(onClick = { }) {
            Text("Round")
        }
    }
}

@Preview
@Composable
private fun RoundButtonOutlinedPreview() = PreviewContainer {
    FooSTheme {
        RoundButton(onClick = { }, outlined = true) {
            Text("Outlined")
        }
    }
}
