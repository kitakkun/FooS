package com.example.foos.ui.view.screen.reaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState
import com.example.foos.ui.view.component.MaxSizeLoadingIndicator
import com.example.foos.ui.view.component.list.ReactionItem
import com.example.foos.ui.view.component.list.ReactionItemList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ReactionScreen(viewModel: ReactionViewModel, navController: NavController) {

    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.fetchNewReactions()
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
        onRefresh = { viewModel.fetchNewReactions(true) }
    ) {
        if (uiState.reactions.isEmpty()) {
            MaxSizeLoadingIndicator()
        } else {
            ReactionItemList(
                uiStates = uiState.reactions,
                onUserIconClick = { userId -> viewModel.onUserIconClick(userId) },
                onContentClick = { viewModel.onContentClick() },
            )
        }
    }

}

