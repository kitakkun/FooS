package com.example.foos.ui.view.screen.reaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState
import com.example.foos.ui.view.component.UserIcon
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ReactionScreen(viewModel: ReactionViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = uiState.value.isRefreshing),
        onRefresh = { viewModel.fetchNewReactions() }
    ) {
        ReactionItemList(
            uiStates = uiState.value.reactions,
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

@Preview
@Composable
fun ReactionItem(
    uiState: ReactionItemUiState = ReactionItemUiState.Default
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        UserIcon(url = uiState.userIcon)
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            val text = stringResource(R.string.reaction_message, uiState.username, uiState.reaction)
            WordEmText(text = text, emIndices = listOf(0))
            Text(
                text = uiState.postContent
            )
        }
    }
}

@Composable
fun WordEmText(
    text: String,
    emIndices: List<Int>,
) {
    val texts = text.split(" ")
    var first = true
    Text(
        buildAnnotatedString {
            for (i in texts.indices) {
                if (!first) {
                    append(" ")
                }
                if (i in emIndices) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(texts[i])
                    }
                } else {
                    append(texts[i])
                }
                first = false
            }
        }
    )
}