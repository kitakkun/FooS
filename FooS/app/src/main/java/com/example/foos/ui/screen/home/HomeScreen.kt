package com.example.foos.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.screen.Screen
import com.example.foos.ui.component.RoundIconActionButton
import com.example.foos.ui.screen.Page
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * ホーム画面のコンポーザブル
 * ユーザーの投稿をリストで閲覧できます。
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()

    viewModel.setNavController(navController)

    SwipeRefresh(state = rememberSwipeRefreshState(
        isRefreshing = uiState.value.isRefreshing),
        onRefresh = { viewModel.fetchNewPosts() }
    ) {
        PostItemList(uiState.value.posts, onContentClick={ postId -> viewModel.onContentClick(postId) }, onPostImageClick = { uris -> viewModel.onPostImageClick(uris) })
    }
    RoundIconActionButton(onClick = { navController.navigate(Page.Post.route) })
}

@Composable
fun PostItemList(
    postItems: List<PostItemUiState>,
    onContentClick: (String) -> Unit = {},
    onPostImageClick: (List<String>) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(postItems) { post ->
            PostItem(post, onContentClick, onPostImageClick)
        }
    }
}

@Composable
fun ReactionRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        FavoriteButton()
        FavoriteButton()
    }

}

@Composable
fun FavoriteButton() {
    var liked by remember { mutableStateOf(false) }
    Image(
        painter = painterResource(if (liked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
        contentDescription = null,
        modifier = Modifier.clickable {
            liked = !liked
        }
    )
}


