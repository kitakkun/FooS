package com.github.kitakkun.foos.ui.view.component.extensions


import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.filter

@Composable
fun LazyGridState.OnAppearLastItem(onAppearLastItem: (Int) -> Unit) {
    val isReachedToListEnd by remember {
        derivedStateOf {
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
