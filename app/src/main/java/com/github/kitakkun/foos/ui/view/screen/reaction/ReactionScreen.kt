package com.github.kitakkun.foos.ui.view.screen.reaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.ui.view.component.list.ReactionItemList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ReactionScreen(viewModel: ReactionViewModel, navController: NavController) {

    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.fetchNewReactions()
    }

    ReactionUI(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchNewReactions(true) },
        isLoading = false, /* TODO: ローディングへの対応 */
        onUserIconClick = { viewModel.onUserIconClick(it) },
        onContentClick = { viewModel.onContentClick(it) },
        reactions = uiState.reactions
    )

}

@Composable
fun ReactionUI(
    reactions: List<ReactionItemUiState>,
    isRefreshing: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh) {
        if (isLoading) {
            MaxSizeLoadingIndicator()
        } else {
            ReactionItemList(
                uiStates = reactions,
                onUserIconClick = onUserIconClick,
                onContentClick = onContentClick,
            )
        }
    }
}

@Preview
@Composable
private fun ReactionUIPreview() = PreviewContainer {
    val list = mutableListOf<ReactionItemUiState>()
    repeat(10) { i ->
        list.add(
            ReactionItemUiState(
                username = "username$i",
                reaction = stringResource(id = com.github.kitakkun.foos.customview.R.string.emoji_like),
                postContent = "post content $i...",
                userIcon = ""
            )
        )
    }
    ReactionUI(
        reactions = list,
        isRefreshing = false,
        isLoading = false,
        onRefresh = {},
        onUserIconClick = {},
        onContentClick = {}
    )
}
