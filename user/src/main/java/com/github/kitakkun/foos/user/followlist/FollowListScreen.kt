package com.github.kitakkun.foos.user.followlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
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

    FollowListUI(
        uiState = uiState,
        fetchFollowee = { viewModel.fetchFollowingUsers() },
        fetchFollower = { viewModel.fetchFollowerUsers() },
        onItemClicked = {
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
    fetchFollowee: () -> Unit,
    fetchFollower: () -> Unit,
    onItemClicked: (String) -> Unit,
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
                    LaunchedEffect(Unit) {
                        fetchFollowee()
                    }
                    UserList(
                        uiStates = uiState.followingUsers,
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
        fetchFollowee = { },
        fetchFollower = { },
        onItemClicked = { },
        onFollowButtonClicked = { },
    )
}
