package com.github.kitakkun.foos.ui.view.screen.followlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.R
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.ui.state.screen.followlist.FollowListScreenUiState
import com.github.kitakkun.foos.ui.state.screen.followlist.UserItemUiState
import com.github.kitakkun.foos.ui.view.component.list.UserList
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

/**
 * フォロワーとフォロー中のユーザリストを表示するスクリーン
 */
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

    FollowListUI(
        uiState = uiState,
        initialPage = initialPage,
        fetchFollowee = { viewModel.fetchFollowees(userId) },
        fetchFollower = { viewModel.fetchFollowers(userId) },
        onItemClicked = { viewModel.navigateToUserProfile(it) },
        onFollowButtonClicked = { /* TODO: フォロー状態の更新 */ }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FollowListUI(
    uiState: FollowListScreenUiState,
    initialPage: Int,
    fetchFollowee: () -> Unit,
    fetchFollower: () -> Unit,
    onItemClicked: (String) -> Unit,
    onFollowButtonClicked: (String) -> Unit,
) {

    val tabTitles = listOf(
        stringResource(id = R.string.following),
        stringResource(id = R.string.followers)
    )

    val pagerState = rememberPagerState(initialPage)

    Column {

        MyTabRow(pagerState = pagerState, tabTitles = tabTitles)

        HorizontalPager(
            state = pagerState,
            count = tabTitles.size,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page: Int ->
            when (page) {
                0 -> {
                    LaunchedEffect(Unit) {
                        fetchFollowee()
                    }
                    UserList(
                        uiStates = uiState.followees,
                        onAppearLastItem = { fetchFollowee() },
                        onItemClicked = onItemClicked,
                        onFollowButtonClicked = onFollowButtonClicked
                    )
                }
                1 -> {
                    LaunchedEffect(Unit) {
                        fetchFollower()
                    }
                    UserList(
                        uiStates = uiState.followers,
                        onAppearLastItem = { fetchFollower() },
                        onItemClicked = onItemClicked,
                        onFollowButtonClicked = onFollowButtonClicked
                    )
                }
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

@Preview
@Composable
private fun FollowListUIPreview() = PreviewContainer {
    val dummyUserList = mutableListOf<UserItemUiState>()
    repeat(10) { i ->
        dummyUserList.add(
            UserItemUiState.Default.copy(
                username = "username$i",
                userId = "userid$i"
            )
        )
    }
    var uiState by remember {
        mutableStateOf(
            FollowListScreenUiState(
                followees = dummyUserList, followers = dummyUserList,
            )
        )
    }
    FollowListUI(
        uiState = uiState,
        initialPage = 0,
        fetchFollowee = { },
        fetchFollower = { },
        onItemClicked = { },
        onFollowButtonClicked = { userId ->
            uiState = uiState.copy(
                followers = uiState.followers.map {
                    if (it.userId == userId) {
                        it.copy(following = !it.following)
                    } else {
                        it
                    }
                },
                followees = uiState.followers.map {
                    if (it.userId == userId) {
                        it.copy(following = !it.following)
                    } else {
                        it
                    }
                }
            )
        }
    )
}
