package com.example.foos.ui.view.component

import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Tabs(
    titles: List<String>,
    tabIndex: Int,
    onClick: (Int, String) -> Unit,
) {
    TabRow(selectedTabIndex = tabIndex) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index,
                onClick = { onClick(index, title) },
                text = { Text(title) }
            )
        }
    }

}