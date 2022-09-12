package com.example.foos.ui.view.screen.followlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.list.UserItem
import com.example.foos.ui.view.component.list.UserList
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

/**
 * フォロワーとフォロー中のユーザリストを表示するスクリーン
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun FollowListScreen(
    viewModel: FollowListViewModel,
    navController: NavController,
    userId: String,
    initialPage: Int = 0
) {

    // ナビゲーションイベントの処理
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    val uiState = viewModel.uiState.value

    val tabTitles = listOf(
        stringResource(id = R.string.following),
        stringResource(id = R.string.followers)
    )

    val pagerState = rememberPagerState(initialPage)
    val coroutineScope = rememberCoroutineScope()

    Column {

        MyTabRow(pagerState = pagerState, tabTitles = tabTitles)

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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MyTabRow(
    pagerState: PagerState,
    tabTitles: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
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
}

@Composable
fun FolloweeList(
    followees: List<UserItemUiState>,
    fetchEvent: () -> Unit,
    onItemClicked: (String) -> Unit,
) {
    SideEffect {
        fetchEvent()
    }
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
    SideEffect {
        fetchEvent()
    }
    UserList(
        uiStates = followers,
        onAppearLastItem = { fetchEvent() },
        onItemClicked = onItemClicked,
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserItemPreview() {
    val uiState = UserItemUiState("userId", "username", "userId", "", "BIO", true, false)
    FooSTheme {
        UserItem(uiState = uiState, onItemClicked = {})
    }
}