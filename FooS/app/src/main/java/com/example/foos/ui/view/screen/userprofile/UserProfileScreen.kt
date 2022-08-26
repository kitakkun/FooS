package com.example.foos.ui.view.screen.userprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.userprofile.UserProfileScreenUiState
import com.example.foos.ui.view.component.Tabs
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.screen.home.PostItemList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

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

    Column() {
        UserProfile(
            username = uiState.value.username,
            userId = uiState.value.userId,
            bio = "",
            profileImage = uiState.value.userIcon,
            followerNum = uiState.value.followerCount,
            followeeNum = uiState.value.followeeCount,
            onFollowButtonClick = {viewModel.onFollowButtonClick()},
            following = uiState.value.following,
        )
        Tabs(
            titles = listOf({ Text("Posts") }, { Text("Reactions") }),
            contents = listOf(
                {
                    TabPosts(listState = listState,
                        uiState = uiState.value,
                        onUserIconClick = { viewModel.onUserIconClick() },
                        onContentClick = { uiState -> viewModel.onContentClick(uiState) },
                        onImageClick = { uiState, clickedImageUrl ->
                            viewModel.onImageClick(
                                uiState,
                                clickedImageUrl
                            )
                        },
                        onAppearLastItem = { viewModel.fetchOlderPosts() }
                    )
                },
                { Text("hello") }
            ),
        )
    }
}

@Composable
fun TabPosts(
    listState: LazyListState,
    uiState: UserProfileScreenUiState,
    onUserIconClick: () -> Unit,
    onContentClick: (PostItemUiState) -> Unit,
    onImageClick: (PostItemUiState, String) -> Unit,
    onAppearLastItem: () -> Unit,
) {

    PostItemList(
        listState = listState,
        uiStates = uiState.posts,
        onUserIconClick = { onUserIconClick.invoke() },
        onContentClick = onContentClick,
        onImageClick = onImageClick,
        onAppearLastItem = { onAppearLastItem.invoke() },
    )
}


/**
 * ユーザプロフィール
 * 名前、プロフ画像、ID
 */
@Composable
fun UserProfile(
    username: String,
    userId: String,
    bio: String,
    profileImage: String,
    followerNum: Int,
    followeeNum: Int,
    following: Boolean,
    onFollowButtonClick: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(url = profileImage, modifier = Modifier.size(70.dp))
            Spacer(Modifier.weight(1f))
            if (userId == Firebase.auth.uid) {
                Button(onClick = onEditButtonClick, shape = RoundedCornerShape(50)) {
                    Text(text = "Edit profile")
                }
            } else {
                Button(onClick = onFollowButtonClick, shape = RoundedCornerShape(50)) {
                    if (following) Text(text = "Following")
                    else Text(text = "Follow")
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
    UserProfile("username", "userId", "users biography...", "", 120, 50, false)
}