package com.github.kitakkun.foos.post.timeline

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.const.paddingLarge
import com.github.kitakkun.foos.common.navigation.BottomSheetRouter
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.composable.button.RoundIconActionButton
import com.github.kitakkun.foos.customview.composable.loading.MaxSizeLoadingIndicator
import com.github.kitakkun.foos.customview.composable.post.PostItemList
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.google.gson.Gson

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.fetchInitialPosts()
        // TODO: Bottomナビゲーションでホームがクリックされたらトップへスクロール(いったんなし）
    }

    HomeUI(
        uiState = uiState,
        listState = listState,
        onPostCreateButtonClick = {
            navController.navigate(PostScreenRouter.PostCreate.route)
        },
        isLoadingPosts = false, /* TODO: ロードインディケータの適切な制御 */
        onAppearLastItem = { viewModel.fetchOlderPosts() },
        onImageClick = { imageUrls, clickedImageUrl ->
            navController.navigate(
                PostScreenRouter.Detail.ImageDetail.routeWithArgs(
                    Uri.encode(Gson().toJson(StringList(imageUrls))),
                    imageUrls.indexOf(clickedImageUrl).toString()
                )
            )
        },
        onUserIconClick = { userId ->
            navController.navigate(UserScreenRouter.UserProfile.routeWithArgs(userId))
        },
        onRefresh = { viewModel.refreshPosts() },
        onContentClick = {
            navController.navigate(PostScreenRouter.Detail.PostDetail.routeWithArgs(it))
        },
        onMoreVertClick = { postId ->
            navController.navigate(BottomSheetRouter.PostOption.route(postId))
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeUI(
    uiState: HomeScreenUiState,
    listState: LazyListState,
    isLoadingPosts: Boolean,
    onRefresh: () -> Unit,
    onUserIconClick: (String) -> Unit,
    onContentClick: (String) -> Unit,
    onImageClick: (List<String>, String) -> Unit,
    onAppearLastItem: () -> Unit,
    onPostCreateButtonClick: () -> Unit,
    onMoreVertClick: (String) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        if (isLoadingPosts) {
            MaxSizeLoadingIndicator()
        } else {
            PostItemList(
                listState = listState,
                uiStates = uiState.posts,
                onUserIconClick = { onUserIconClick(it) },
                onContentClick = { onContentClick(it) },
                onImageClick = { imageUrls, clickedUrl -> onImageClick(imageUrls, clickedUrl) },
                onAppearLastItem = { onAppearLastItem() },
                onMoreVertClick = onMoreVertClick,
            )
        }
        RoundIconActionButton(
            icon = Icons.Filled.Add,
            onClick = onPostCreateButtonClick,
            modifier = Modifier.padding(paddingLarge)
        )
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun HomeUIPreview() = PreviewContainer {
    HomeUI(
        uiState = HomeScreenUiState.Default,
        listState = rememberLazyListState(),
        isLoadingPosts = false,
        onRefresh = {},
        onUserIconClick = {},
        onContentClick = {},
        onImageClick = { _, _ -> },
        onAppearLastItem = {},
        onPostCreateButtonClick = {},
        onMoreVertClick = {},
    )
}
