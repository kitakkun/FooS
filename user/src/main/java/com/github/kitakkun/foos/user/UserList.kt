package com.github.kitakkun.foos.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.common.ext.OnAppearLastItem

@Composable
fun UserList(
    uiStates: List<UserItemUiState>,
    onAppearLastItem: (Int) -> Unit,
    onItemClicked: (String) -> Unit,
    onFollowButtonClicked: (String) -> Unit,
) {
    val state = rememberLazyListState()
    state.OnAppearLastItem(onAppearLastItem = onAppearLastItem)
    LazyColumn(
        state = state
    ) {
        items(uiStates) {
            UserItem(
                uiState = it,
                onItemClicked = { onItemClicked(it.userId) },
                onFollowButtonClicked = { onFollowButtonClicked(it.userId) },
            )
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}
