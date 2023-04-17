package com.github.kitakkun.foos.ui.navgraph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.post.create.PostScreen
import com.github.kitakkun.foos.post.create.PostViewModelImpl
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmScreen
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmViewModelImpl
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectScreen
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectViewModelImpl
import com.github.kitakkun.foos.post.imagedetail.ImageDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailViewModelImpl
import com.github.kitakkun.foos.user.followlist.FollowListScreen
import com.github.kitakkun.foos.user.followlist.FollowListViewModelImpl
import com.github.kitakkun.foos.user.profile.UserProfileScreen
import com.github.kitakkun.foos.user.profile.UserProfileViewModelImpl

fun NavGraphBuilder.subGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    navigation(
        route = SubScreen.PostCreate.route,
        startDestination = SubScreen.PostCreate.Edit.route,
    ) {
        composable(SubScreen.PostCreate.Edit.route) {
            val vm: PostViewModelImpl = hiltViewModel()
            PostScreen(vm, navController, screenViewModel)
        }
        composable(SubScreen.PostCreate.LocationSelect.route) {
            val vm: LocationSelectViewModelImpl = hiltViewModel()
            LocationSelectScreen(vm, navController, screenViewModel)
        }
        composable(SubScreen.PostCreate.LocationConfirm.route) {
            val vm: LocationConfirmViewModelImpl = hiltViewModel()
            LocationConfirmScreen(vm, navController, screenViewModel)
        }
    }
    composable(
        SubScreen.UserProfile.routeWithParam,
        SubScreen.UserProfile.arguments,
    ) {
        val userId = it.arguments?.getString(SubScreen.UserProfile.key(0))
        userId?.let {
            val vm: UserProfileViewModelImpl = hiltViewModel()
            UserProfileScreen(vm, navController, userId)
        }
    }
    composable(
        SubScreen.PostDetail.routeWithParam,
        SubScreen.PostDetail.arguments,
    ) {
        val postId = it.arguments?.getString(SubScreen.PostDetail.key(0))
        postId?.let {
            val vm: PostDetailViewModelImpl = hiltViewModel()
            PostDetailScreen(vm, navController, postId)
        }
    }
    composable(
        SubScreen.ImageDetail.routeWithParam,
        SubScreen.ImageDetail.arguments,
    ) {
        val imageUrls =
            it.arguments?.getParcelable<StringList>(SubScreen.ImageDetail.key(0))
        val clickedImageIndex = it.arguments?.getString(SubScreen.ImageDetail.key(1))
        if (imageUrls != null && clickedImageIndex != null) {
            ImageDetailScreen(
                navController = navController,
                imageUrls = imageUrls.value,
                initialIndex = clickedImageIndex.toInt(),
            )
        }
    }
    composable(
        SubScreen.FollowList.routeWithParam,
        SubScreen.FollowList.arguments,
    ) {
        val userId = it.arguments?.getString(SubScreen.FollowList.key(0))
        val followees = it.arguments?.getBoolean(SubScreen.FollowList.key(1))
        if (userId != null && followees != null) {
            val index = if (followees) 0 else 1
            val vm: FollowListViewModelImpl = hiltViewModel()
            FollowListScreen(
                viewModel = vm,
                userId = userId,
                initialPage = index,
                navController = navController
            )
        }
    }
}
