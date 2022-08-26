package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.*

@Composable
fun Tabs(
    titles: List<@Composable() () -> Unit>,
    contents: List<@Composable() () -> Unit>
) {
    if (titles.size != contents.size) { return }
    var tabIndex by remember { mutableStateOf(0) }
    Column() {
        TabRow(selectedTabIndex = tabIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { title() }
                )
            }
        }
        contents[tabIndex]()
    }

}