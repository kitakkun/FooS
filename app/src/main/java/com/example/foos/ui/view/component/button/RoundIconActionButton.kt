package com.example.foos.ui.view.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.ui.theme.FooSTheme

@Composable
fun BoxScope.RoundIconActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { onClick.invoke() },
        modifier = modifier.align(Alignment.BottomEnd),
    ) {
        Icon(icon, contentDescription = "", tint = MaterialTheme.colors.onSecondary)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RoundIconActionButtonPreview() {
    FooSTheme {
        Box() {
            RoundIconActionButton(
                icon = Icons.Filled.Add,
                onClick = {}
            )
        }
    }
}
