package com.example.foos.ui.view.screen.userprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.screen.home.OnAppearLastItem
import com.example.foos.ui.view.screen.home.PostItem
import com.example.foos.ui.view.screen.home.PostItemList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(
    ExperimentalPagerApi::class,
)
@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    listState.OnAppearLastItem(onAppearLastItem = { viewModel.fetchOlderPosts() })

    LaunchedEffect(Unit) {
        viewModel.navigateRouteFlow.collect {
            navController.navigate(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.scrollUpEvent.collect {
            if (it) listState.animateScrollToItem(0, 0)
        }
    }

    val nestedScrollViewState = rememberNestedScrollViewState()
    // this layout produce buggy scroll if we don't use jetpack compose alpha02 or above.
    // nestedScroll's bug exists until alpha01.
    VerticalNestedScrollView(
        state = nestedScrollViewState,
        header = {
            UserProfileView(
                username = uiState.value.username,
                userId = uiState.value.userId,
                bio = "",
                profileImage = uiState.value.userIcon,
                followerNum = uiState.value.followerCount,
                followeeNum = uiState.value.followeeCount,
                onFollowButtonClick = { viewModel.onFollowButtonClick() },
                following = uiState.value.following,
            )
        },
        content = {
            Column {
                val tabList = listOf(
                    stringResource(id = R.string.tab_posts),
                    stringResource(id = R.string.tab_reactions)
                )
                val pagerState = rememberPagerState(initialPage = 0)
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

                HorizontalPager(
                    state = pagerState,
                    count = tabList.size,
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Top
                ) { page: Int ->
                    when (page) {
                        0 -> PostItemList(
                            uiStates = uiState.value.posts,
                            onImageClick = { state, url ->
                                viewModel.onImageClick(
                                    state,
                                    url
                                )
                            },
                            onContentClick = { state -> viewModel.onContentClick(state) },
                            onUserIconClick = { viewModel.onUserIconClick() },
                            onAppearLastItem = { viewModel.fetchOlderPosts() },
                        )
                        1 -> LazyColumn(
                        ) {
                            item {
                                Text(text = "Reaction Items will come here.")
                            }
                        }
                    }
                }
            }
        }
    )
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
    onFollowButtonClick: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
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
                Button(onClick = onFollowButtonClick, shape = RoundedCornerShape(50)) {
                    if (following) Text(text = stringResource(id = R.string.following))
                    else Text(text = stringResource(id = R.string.follow))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(username, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(userId, fontWeight = FontWeight.Light, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Biography(bio = bio)
        FollowInfo(followerNum = followerNum, followeeNum = followeeNum)
    }

}

@Composable
fun Biography(
    bio: String
) {
    Text(bio)
}

/**
 * フォロー情報
 * @param followerNum フォロワー数
 * @param followeeNum フォロー数
 */
@Composable
fun FollowInfo(
    followerNum: Int,   // フォロワー数
    followeeNum: Int,   // フォロー数
) {
    Row(
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$followeeNum ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)) {
                    append(stringResource(id = R.string.following))
                }
            },
            modifier = Modifier.padding(16.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$followerNum ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)) {
                    append(stringResource(id = R.string.followers))
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserProfilePreview() {
    UserProfileView("username", "userId", "users biography...", "", 120, 50, false)
}