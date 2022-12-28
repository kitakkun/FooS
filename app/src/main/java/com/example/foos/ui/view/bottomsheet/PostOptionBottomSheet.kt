package com.example.foos.ui.view.bottomsheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foos.R
import com.example.foos.ui.state.component.MenuItemUiState
import com.example.foos.ui.view.component.menu.MenuItem

@Composable
fun PostOptionBottomSheet(
    viewModel: PostOptionViewModel = hiltViewModel<PostOptionViewModelImpl>(),
    postId: String,
) {
    LazyColumn {
        item {
            MenuItem(
                uiState = MenuItemUiState(
                    text = R.string.delete_post,
                    icon = R.drawable.ic_delete_post,
                    onClick = {
                        /* TODO: UIStateの中にラムダのイベント入ってるのよくない。直す */
                        viewModel.onDeleteClick(postId)
                    },
                ),
            )
        }
    }
}