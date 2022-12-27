package com.example.foos.ui.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foos.ui.navigation.MainScreen
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.navigation.navargs.StringList
import com.example.foos.ui.view.screen.followlist.FollowListScreen
import com.example.foos.ui.view.screen.followlist.FollowListViewModelImpl
import com.example.foos.ui.view.screen.home.HomeScreen
import com.example.foos.ui.view.screen.home.HomeViewModelImpl
import com.example.foos.ui.view.screen.imagedetail.ImageDetailScreen
import com.example.foos.ui.view.screen.locationconfirm.LocationConfirmScreen
import com.example.foos.ui.view.screen.locationconfirm.LocationConfirmViewModel
import com.example.foos.ui.view.screen.locationselect.LocationSelectScreen
import com.example.foos.ui.view.screen.locationselect.LocationSelectViewModel
import com.example.foos.ui.view.screen.map.MapScreen
import com.example.foos.ui.view.screen.map.MapViewModelImpl
import com.example.foos.ui.view.screen.post.PostScreen
import com.example.foos.ui.view.screen.post.PostViewModelImpl
import com.example.foos.ui.view.screen.postdetail.PostDetailScreen
import com.example.foos.ui.view.screen.postdetail.PostDetailViewModelImpl
import com.example.foos.ui.view.screen.reaction.ReactionScreen
import com.example.foos.ui.view.screen.reaction.ReactionViewModelImpl
import com.example.foos.ui.view.screen.setting.SettingScreen
import com.example.foos.ui.view.screen.setting.SettingViewModelImpl
import com.example.foos.ui.view.screen.userprofile.UserProfileScreen
import com.example.foos.ui.view.screen.userprofile.UserProfileViewModelImpl

/**
 * 画面下部ナビゲーションのNavHost
 */
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    screenViewModel: ScreenViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.Home.route,
        Modifier.padding(innerPadding)
    ) {
        composable(MainScreen.Home.route) {
            val vm: HomeViewModelImpl = hiltViewModel()
            HomeScreen(vm, navController, screenViewModel)
        }
        composable(MainScreen.Map.route) {
            val vm: MapViewModelImpl = hiltViewModel()
            MapScreen(vm, navController)
        }
        composable(MainScreen.Reaction.route) {
            val vm: ReactionViewModelImpl = hiltViewModel()
            ReactionScreen(vm, navController)
        }
        composable(MainScreen.Setting.route) {
            val vm: SettingViewModelImpl = hiltViewModel()
            SettingScreen(vm, screenViewModel)
        }
        composable(SubScreen.PostCreate.route) {
            val vm: PostViewModelImpl = hiltViewModel()
            PostScreen(vm, navController, screenViewModel)
        }
        composable(SubScreen.PostCreate.LocationSelect.route) {
            val vm: LocationSelectViewModel = hiltViewModel()
            LocationSelectScreen(vm, navController, screenViewModel)
        }
        composable(SubScreen.PostCreate.LocationConfirm.route) {
            val vm: LocationConfirmViewModel = hiltViewModel()
            LocationConfirmScreen(vm, navController, screenViewModel)
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
}
