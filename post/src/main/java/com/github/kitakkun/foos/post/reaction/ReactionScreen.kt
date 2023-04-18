package com.github.kitakkun.foos.post.reaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.kitakkun.foos.customview.R
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun ReactionScreen(
    viewModel: ReactionViewModelImpl = hiltViewModel(),
    navController: NavController
) {

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReactionUI(
    reactions: List<ReactionItemUiState>,
    isRefreshing: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        if (isLoading) {
            MaxSizeLoadingIndicator()
        } else {
            ReactionItemList(
                uiStates = reactions,
                onUserIconClick = onUserIconClick,
                onContentClick = onContentClick,
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
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
                reaction = stringResource(id = R.string.emoji_like),
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
