package com.example.foos.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foos.R
import com.example.foos.ui.Screen
import com.example.foos.ui.component.Post
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, navController: NavController) {

    val homeUiState = homeViewModel.homeUiState.collectAsState()

    SwipeRefresh(state = rememberSwipeRefreshState(
        isRefreshing = homeUiState.value.isRefreshing),
        onRefresh = {
            homeViewModel.fetchNewerPosts()
        }
    ) {
        PostItemList(homeUiState.value.posts)
    }
    RoundIconActionButton(onClick = { navController.navigate(Screen.Post.route) })
}

@Composable
fun PostItemList(
    postItems: List<PostItemUiState>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(postItems) { post ->
            Post(post)
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


