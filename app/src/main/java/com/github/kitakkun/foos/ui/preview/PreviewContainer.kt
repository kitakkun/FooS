package com.github.kitakkun.foos.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.ui.theme.FooSTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit,
) {
    FooSTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
private fun ThemeAppliedPreviewContainerPreview() = PreviewContainer {
    PreviewContainer {
        Column {
            Text(text = "Theme applied")
            Button(onClick = { }) {
                Text(text = "Sample Button")
            }
        }
    }
}

@Preview
@Composable
private fun NonThemeAppliedPreview() = PreviewContainer {
    Column {
        Text(text = "Theme not applied")
        Button(onClick = { }) {
            Text(text = "Sample Button")
        }
    }
}
