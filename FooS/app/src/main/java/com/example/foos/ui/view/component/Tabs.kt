package com.example.foos.ui.view.component

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Tabs(
    titles: List<String>,
    tabIndex: Int,
    onClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(selectedTabIndex = tabIndex, modifier = modifier) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index,
                onClick = { onClick(index, title) },
                text = { Text(title) }
            )
        }
    }

}