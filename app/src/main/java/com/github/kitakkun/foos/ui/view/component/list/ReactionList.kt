package com.github.kitakkun.foos.ui.view.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.kitakkun.foos.ui.constants.paddingMedium
import com.github.kitakkun.foos.ui.state.screen.reaction.ReactionItemUiState

@Composable
fun ReactionItemList(
    uiStates: List<ReactionItemUiState>,
    onUserIconClick: (String) -> Unit = {},
    onContentClick: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(paddingMedium)
    )
    {
        items(uiStates) {
            ReactionItem(it)
        }
    }
}
