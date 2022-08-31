package com.example.foos.ui.view.screen.postdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.foos.ui.view.component.PostDetailView

@Composable
fun PostDetailScreen(viewModel: PostDetailViewModel, navController: NavController, postId: String) {

    LaunchedEffect(Unit) {
        viewModel.fetch(postId)
    }

    val uiState = viewModel.uiState.value
    val postItemUiState = uiState.postItemUiState

    PostDetailView(
        uiState = postItemUiState,
        onUserInfoClicked = { viewModel.onUserInfoClicked() },
        onReactionButtonClicked = { viewModel.onReactionButtonClicked(it) },
        onReactionRemoved = { viewModel.onReactionRemoved() },
        onGoogleMapsClicked = { viewModel.onGoogleMapsClicked() }
    )
}

