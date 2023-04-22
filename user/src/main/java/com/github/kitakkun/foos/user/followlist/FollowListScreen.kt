package com.github.kitakkun.foos.user.followlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.UserList
import com.github.kitakkun.foos.user.composable.UserItemUiState
import kotlinx.coroutines.launch

/**
 * フォロワーとフォロー中のユーザリストを表示するスクリーン
 */
@Composable
fun FollowListScreen(
    viewModel: FollowListViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFollowerUsers()
        viewModel.fetchFollowingUsers()
    }

    FollowListUI(
        uiState = uiState,
        onRefreshFollowingUserList = viewModel::fetchFollowingUsers,
        onRefreshFollowerUserList = viewModel::fetchFollowerUsers,
        onUserItemClicked = {
            navController.navigate(
                UserScreenRouter.UserProfile.routeWithArgs(it)
            )
        },
        onFollowButtonClicked = viewModel::toggleFollowState,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FollowListUI(
    uiState: FollowListScreenUiState,
    onRefreshFollowingUserList: () -> Unit,
    onRefreshFollowerUserList: () -> Unit,
    onUserItemClicked: (userId: String) -> Unit,
    onFollowButtonClicked: (userId: String) -> Unit,
) {
    val tabTitles = listOf(
        stringResource(id = R.string.following),
        stringResource(id = R.string.followers)
    )

    val pagerState = rememberPagerState(
        initialPage = if (uiState.shouldShowFollowingListFirst) 0 else 1,
    )

    Column {
        MyTabRow(pagerState = pagerState, tabTitles = tabTitles)
        HorizontalPager(
            state = pagerState,
            pageCount = tabTitles.size,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page: Int ->
            when (page) {
                0 -> {
                    FollowUserList(
                        users = uiState.followingUsers,
                        isRefreshing = uiState.isFollowingListRefreshing,
                        onAppearLastItem = onRefreshFollowingUserList,
                        onItemClicked = onUserItemClicked,
                        onFollowButtonClicked = onFollowButtonClicked,
                        onRefresh = onRefreshFollowingUserList,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                1 -> {
                    FollowUserList(
                        users = uiState.followers,
                        isRefreshing = uiState.isFollowerListRefreshing,
                        onAppearLastItem = onRefreshFollowerUserList,
                        onItemClicked = onUserItemClicked,
                        onFollowButtonClicked = onFollowButtonClicked,
                        onRefresh = onRefreshFollowerUserList,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FollowUserList(
    users: List<UserItemUiState>,
    onAppearLastItem: () -> Unit,
    onItemClicked: (userId: String) -> Unit,
    onFollowButtonClicked: (userId: String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = modifier.pullRefresh(state = pullRefreshState)
    ) {
        UserList(
            uiStates = users,
            onAppearLastItem = { onAppearLastItem() },
            onItemClicked = onItemClicked,
            onFollowButtonClicked = onFollowButtonClicked,
            modifier = Modifier.fillMaxSize()
        )
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyTabRow(
    pagerState: PagerState,
    tabTitles: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage
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
    FollowListUI(
        uiState = FollowListScreenUiState.buildTestData(),
        onRefreshFollowingUserList = { },
        onRefreshFollowerUserList = { },
        onUserItemClicked = { },
        onFollowButtonClicked = { },
    )
}
