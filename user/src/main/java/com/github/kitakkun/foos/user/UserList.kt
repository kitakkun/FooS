package com.github.kitakkun.foos.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.common.ext.OnAppearLastItem
import com.github.kitakkun.foos.user.composable.UserItem
import com.github.kitakkun.foos.user.composable.UserItemUiState

@Composable
fun UserList(
    uiStates: List<UserItemUiState>,
    onAppearLastItem: (Int) -> Unit,
    onItemClicked: (String) -> Unit,
    onFollowButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    state.OnAppearLastItem(onAppearLastItem = onAppearLastItem)
    LazyColumn(
        state = state,
        modifier = modifier,
    ) {
        items(uiStates) {
            UserItem(
                uiState = it,
                onClick = { onItemClicked(it.id) },
                onFollowButtonClicked = { onFollowButtonClicked(it.id) },
            )
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}
