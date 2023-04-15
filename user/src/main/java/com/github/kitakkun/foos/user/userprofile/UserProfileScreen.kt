package com.github.kitakkun.foos.user.userprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.composable.post.PostItemList
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.composable.user.VerticalUserIdentityText
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.FollowButton
import com.github.kitakkun.foos.user.FollowInfo
import com.github.kitakkun.foos.user.R
import com.google.accompanist.pager.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    navController: NavController,
    userId: String
) {

    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchUserInfo(
                userId = userId,
                onFinished = {
                    viewModel.fetchInitialPosts()
                    viewModel.fetchInitialMediaPosts()
                    viewModel.fetchInitialReactedPosts()
                }
            )
        }
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    val nestedScrollViewState = rememberNestedScrollViewState()
    val pagerState = rememberPagerState(initialPage = 0)

    // this layout produce buggy scroll if we don't use jetpack compose alpha02 or above.
    // nestedScroll's bug exists until alpha01.
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        when (pagerState.currentPage) {
            0 -> viewModel.refreshPosts()
            1 -> viewModel.refreshMediaPosts()
            else -> viewModel.refreshReactedPosts()
        }
    }) {
        VerticalNestedScrollView(
            state = nestedScrollViewState,
            header = {
                UserProfileView(
                    username = uiState.username,
                    userId = uiState.userId,
                    bio = "biography (not available for now)",
                    profileImage = uiState.userIcon,
                    followerNum = uiState.followerCount,
                    followeeNum = uiState.followeeCount,
                    onFollowingTextClick = { viewModel.navigateToFolloweeList(userId = uiState.userId) },
                    onFollowersTextClick = { viewModel.navigateToFollowerList(userId = uiState.userId) },
                    onFollowButtonClick = { viewModel.onFollowButtonClick() },
                    following = uiState.following,
                )
            },
            content = {
                Column {
                    val tabList = listOf(
                        stringResource(id = R.string.tab_posts),
                        stringResource(id = R.string.tab_media),
                        stringResource(id = R.string.tab_reactions),
                    )

                    MyTabRow(pagerState = pagerState, tabList = tabList)

                    MyPager(
                        pagerState = pagerState, pageCount = tabList.size,
                        pageContents = listOf(
                            {
                                if (uiState.isLoadingPosts) {
                                    MaxSizeLoadingIndicator()
                                } else {
                                    PostItemList(
                                        uiStates = uiState.posts,
                                        onImageClick = { imageUrls, clickedImageUrl ->
                                            viewModel.onImageClick(
                                                imageUrls,
                                                clickedImageUrl
                                            )
                                        },
                                        onContentClick = { viewModel.onContentClick(it) },
                                        onUserIconClick = { viewModel.onUserIconClick(it) },
                                        onAppearLastItem = { viewModel.fetchOlderPosts() },
                                        onMoreVertClick = viewModel::onMoreVertClick,
                                    )
                                }
                            },
                            {
                                if (uiState.isLoadingMediaPosts) {
                                    MaxSizeLoadingIndicator()
                                } else {
                                    MediaPostGrid(
                                        uiStates = uiState.mediaPosts,
                                        onContentClick = { viewModel.onContentClick(it) },
                                        onAppearLastItem = { viewModel.fetchOlderMediaPosts() },
                                    )
                                }
                            },
                            {
                                if (uiState.isLoadingUserReactedPosts) {
                                    MaxSizeLoadingIndicator()
                                } else {
                                    PostItemList(
                                        uiStates = uiState.userReactedPosts,
                                        onImageClick = { imageUrls, clickedImageUrl ->
                                            viewModel.onImageClick(
                                                imageUrls,
                                                clickedImageUrl
                                            )
                                        },
                                        onContentClick = { viewModel.onContentClick(it) },
                                        onUserIconClick = { viewModel.onUserIconClick(it) },
                                        onAppearLastItem = { viewModel.fetchOlderUserReactedPosts() },
                                        onMoreVertClick = viewModel::onMoreVertClick,
                                    )
                                }
                            },
                        )
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MyTabRow(
    pagerState: PagerState,
    tabList: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        tabList.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MyPager(
    pagerState: PagerState,
    pageCount: Int,
    pageContents: List<@Composable () -> Unit>
) {
    HorizontalPager(
        state = pagerState,
        count = pageCount,
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.Top
    ) { page: Int ->
        pageContents[page]()
    }
}

/**
 * ユーザプロフィール
 * 名前、プロフ画像、ID
 */
@Composable
fun UserProfileView(
    username: String,
    userId: String,
    bio: String,
    profileImage: String,
    followerNum: Int,
    followeeNum: Int,
    following: Boolean,
    onFollowingTextClick: () -> Unit = {},
    onFollowersTextClick: () -> Unit = {},
    onFollowButtonClick: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(paddingMedium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(url = profileImage, modifier = Modifier.size(70.dp))
            Spacer(Modifier.weight(1f))
            if (userId == Firebase.auth.uid) {
                Button(onClick = onEditButtonClick, shape = RoundedCornerShape(50)) {
                    Text(text = stringResource(id = R.string.edit_profile))
                }
            } else {
                FollowButton(onClick = onFollowButtonClick, following = following)
            }
        }
        Spacer(Modifier.height(paddingMedium))
        VerticalUserIdentityText(username = username, userId = userId)
        Spacer(Modifier.height(paddingMedium))
        Text(bio)
        FollowInfo(
            followerNum = followerNum,
            followeeNum = followeeNum,
            onFollowingTextClick = onFollowingTextClick,
            onFollowersTextClick = onFollowersTextClick,
        )
    }

}

@Preview
@Composable
fun UserProfilePreview() = PreviewContainer {
    UserProfileView("username", "userId", "users biography...", "", 120, 50, false)
}
