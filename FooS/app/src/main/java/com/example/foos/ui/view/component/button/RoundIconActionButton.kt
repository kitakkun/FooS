package com.example.foos.ui.view.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.foos.ui.theme.FooSTheme

@Composable
fun RoundIconActionButton(icon: ImageVector, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(icon, contentDescription = "", tint = MaterialTheme.colors.onSecondary)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RoundIconActionButtonPreview() {
    FooSTheme {
        RoundIconActionButton(
            icon = Icons.Filled.Add,
            onClick = {}
        )
    }
}
