package com.github.kitakkun.foos.user.followlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.ext.OnAppearLastItem
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.com.github.kitakkun.foos.user.followlist.FollowUserUiState
import com.github.kitakkun.foos.user.composable.FollowUserItem
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
        onRefreshFollowingUserList = { viewModel.fetchFollowingUsers(isRefresh = true) },
        onRefreshFollowerUserList = { viewModel.fetchFollowerUsers(isRefresh = true) },
        onUserItemClicked = {
            navController.navigate(
                UserScreenRouter.UserProfile.routeWithArgs(it)
            )
        },
        onFollowButtonClicked = viewModel::toggleFollowState,
        onAppearLastFollowerUserItem = viewModel::fetchMoreFollowerUsers,
        onAppearLastFollowingUserItem = viewModel::fetchMoreFollowingUsers,
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
    onAppearLastFollowingUserItem: () -> Unit,
    onAppearLastFollowerUserItem: () -> Unit,
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
                        onAppearLastItem = onAppearLastFollowingUserItem,
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
                        onAppearLastItem = onAppearLastFollowerUserItem,
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
    users: List<FollowUserUiState>,
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
    val listState = rememberLazyListState()
    listState.OnAppearLastItem(onAppearLastItem = { onAppearLastItem() })
    Box(
        modifier = modifier.pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            state = listState,
        ) {
            items(users) { user ->
                FollowUserItem(
                    uiState = user,
                    onClick = { onItemClicked.invoke(user.id) },
                    onFollowButtonClicked = { onFollowButtonClicked.invoke(user.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Divider(thickness = 1.dp, color = Color.LightGray)
            }
        }
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
        onAppearLastFollowingUserItem = { },
        onAppearLastFollowerUserItem = { },
    )
}
