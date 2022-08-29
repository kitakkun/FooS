package com.example.foos.ui.view.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.filter

@Composable
fun LazyListState.OnAppearLastItem(onAppearLastItem: (Int) -> Unit) {
    val isReachedToListEnd by remember {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.size < layoutInfo.totalItemsCount &&
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isReachedToListEnd }
            .filter { it }
            .collect {
                onAppearLastItem(layoutInfo.totalItemsCount)
            }
    }
}
