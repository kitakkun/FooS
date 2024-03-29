package com.github.kitakkun.foos.post.bottomsheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.github.kitakkun.foos.customview.composable.menu.MenuItem
import com.github.kitakkun.foos.customview.composable.menu.MenuItemUiState
import com.github.kitakkun.foos.post.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostOptionBottomSheet(
    viewModel: PostOptionViewModel = koinViewModel(),
    navController: NavController,
    postId: String,
) {
    LaunchedEffect(Unit) {
        viewModel.navUpEvent.collect {
            navController.navigateUp()
        }
    }
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
