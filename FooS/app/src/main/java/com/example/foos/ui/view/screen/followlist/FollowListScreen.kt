package com.example.foos.ui.view.screen.followlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.view.component.FollowButton
import com.example.foos.ui.view.component.OnAppearLastItem
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.component.VerticalUserIdentityText
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FollowListScreen(viewModel: FollowListViewModel, navController: NavController, userId: String, initialPage: Int = 0) {
    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchFollowers(userId)
        viewModel.fetchFollowees(userId)
    }

    val tabTitles = listOf(
        stringResource(id = R.string.following),
        stringResource(id = R.string.followers)
    )

    val pagerState = rememberPagerState(initialPage)
    val coroutineScope = rememberCoroutineScope()

    Column() {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            count = tabTitles.size,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page: Int ->
            when (page) {
                0 -> FolloweeList(
                    followees = uiState.followees,
                    fetchEvent = { viewModel.fetchFollowees(userId) },
                    onItemClicked = { id -> viewModel.navigateToUserProfile(id) }
                )
                1 -> FollowerList(
                    followers = uiState.followers,
                    fetchEvent = { viewModel.fetchFollowers(userId) },
                    onItemClicked = { id -> viewModel.navigateToUserProfile(id) },
                )
            }
        }
    }

}

@Composable
fun FolloweeList(
    followees: List<UserItemUiState>,
    fetchEvent: () -> Unit,
    onItemClicked: (String) -> Unit,
) {
    UserList(
        uiStates = followees,
        onAppearLastItem = { fetchEvent() },
        onItemClicked = onItemClicked,
    )
}

@Composable
fun FollowerList(
    followers: List<UserItemUiState>,
    fetchEvent: () -> Unit,
    onItemClicked: (String) -> Unit,
) {
    UserList(
        uiStates = followers,
        onAppearLastItem = { fetchEvent() },
        onItemClicked = onItemClicked,
    )
}

@Composable
fun UserList(
    uiStates: List<UserItemUiState>,
    onAppearLastItem: (Int) -> Unit,
    onItemClicked: (String) -> Unit,
) {
    val state = rememberLazyListState()
    state.OnAppearLastItem(onAppearLastItem = onAppearLastItem)
    LazyColumn(
        state = state
    ) {
        items(uiStates) {
            UserItem(uiState = it, onItemClicked = onItemClicked)
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun UserItem(
    uiState: UserItemUiState,
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(uiState.userId) },
    ) {
        if (uiState.followingYou) {
            Text(text = "follows you", fontWeight = FontWeight.Light)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                UserIcon(url = uiState.profileImage)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row {
                        VerticalUserIdentityText(
                            username = uiState.username,
                            userId = uiState.userId,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = uiState.bio)
                }
            }
            if (uiState.userId != Firebase.auth.uid) {
                FollowButton(onClick = { /*TODO*/ }, following = uiState.following)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserItemPreview() {
    val uiState = UserItemUiState("username", "userId", "", "BIO", true, false)
    UserItem(uiState = uiState, onItemClicked = {})
}