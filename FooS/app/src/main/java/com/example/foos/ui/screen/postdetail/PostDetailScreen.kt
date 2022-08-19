package com.example.foos.ui.screen.postdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.foos.ui.component.AsyncUserIcon

@Composable
fun PostDetailScreen(viewModel: PostDetailViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()
    val postItemUiState = uiState.value.postItemUiState

    Column {
        Row {
            AsyncUserIcon(url = postItemUiState.userIcon)
            Text(postItemUiState.content)
        }
    }
}