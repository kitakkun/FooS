package com.example.foos.ui.view.screen.reaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState
import com.example.foos.ui.view.component.list.ReactionItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ReactionScreen(viewModel: ReactionViewModel, navController: NavController) {

    val uiState = viewModel.uiState.value

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
        onRefresh = { viewModel.fetchNewReactions() }
    ) {
        ReactionItemList(
            uiStates = uiState.reactions,
            onUserIconClick = { userId -> viewModel.onUserIconClick(userId) },
            onContentClick = { viewModel.onContentClick() },
        )
    }

}

@Composable
fun ReactionItemList(
    uiStates: List<ReactionItemUiState>,
    onUserIconClick: (String) -> Unit = {},
    onContentClick: () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        items(uiStates) {
            ReactionItem(it)
        }
    }
}
