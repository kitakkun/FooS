package com.example.foos.ui.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foos.ui.navigation.Screen
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.navigation.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.view.screen.followlist.FollowListScreen
import com.example.foos.ui.view.screen.followlist.FollowListViewModel
import com.example.foos.ui.view.screen.home.HomeScreen
import com.example.foos.ui.view.screen.home.HomeViewModel
import com.example.foos.ui.view.screen.imagedetail.ImageDetailScreen
import com.example.foos.ui.view.screen.locationconfirm.LocationConfirmScreen
import com.example.foos.ui.view.screen.locationconfirm.LocationConfirmViewModel
import com.example.foos.ui.view.screen.locationselect.LocationSelectScreen
import com.example.foos.ui.view.screen.locationselect.LocationSelectViewModel
import com.example.foos.ui.view.screen.map.MapScreen
import com.example.foos.ui.view.screen.map.MapViewModel
import com.example.foos.ui.view.screen.post.PostScreen
import com.example.foos.ui.view.screen.post.PostViewModel
import com.example.foos.ui.view.screen.postdetail.PostDetailScreen
import com.example.foos.ui.view.screen.postdetail.PostDetailViewModel
import com.example.foos.ui.view.screen.reaction.ReactionScreen
import com.example.foos.ui.view.screen.reaction.ReactionViewModel
import com.example.foos.ui.view.screen.setting.SettingScreen
import com.example.foos.ui.view.screen.setting.SettingViewModel
import com.example.foos.ui.view.screen.userprofile.UserProfileScreen
import com.example.foos.ui.view.screen.userprofile.UserProfileViewModel

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
        startDestination = Screen.Home.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(vm, navController, screenViewModel)
        }
        composable(Screen.Map.route) {
            val vm: MapViewModel = hiltViewModel()
            MapScreen(vm, navController)
        }
        composable(Screen.Reaction.route) {
            val vm: ReactionViewModel = hiltViewModel()
            ReactionScreen(vm, navController)
        }
        composable(Screen.Setting.route) {
            val vm: SettingViewModel = hiltViewModel()
            SettingScreen(vm)
        }
        composable(SubScreen.PostCreate.route) {
            val vm: PostViewModel = hiltViewModel()
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
            val userId = SubScreen.PostDetail.resolveArguments(it.arguments)["userId"] as String?
            userId?.let {
                val vm: UserProfileViewModel = hiltViewModel()
                vm.setUserId(userId)
                UserProfileScreen(vm, navController)
            }
        }
        composable(
            SubScreen.PostDetail.routeWithParam,
            SubScreen.PostDetail.arguments,
        ) {
            val userId = SubScreen.PostDetail.resolveArguments(it.arguments)["userId"] as String?
            userId?.let {
                val vm: PostDetailViewModel = hiltViewModel()
                PostDetailScreen(vm, navController, userId)
            }
        }
        composable(
            SubScreen.ImageDetail.routeWithParam,
            SubScreen.ImageDetail.arguments,
        ) {
            val uiStateWithImageUrl =
                SubScreen.PostDetail.resolveArguments(it.arguments)["uiStateWithImageUrl"] as PostItemUiStateWithImageUrl?
            uiStateWithImageUrl?.let {
                ImageDetailScreen(navController = navController, post = uiStateWithImageUrl)
            }
        }
        composable(
            SubScreen.FollowList.routeWithParam,
            SubScreen.FollowList.arguments,
        ) {
            val params = SubScreen.FollowList.resolveArguments(it.arguments)
            val userId = params["userId"] as String?
            val followees = params["followees"] as Boolean?
            if (userId != null && followees != null) {
                val index = if (followees) 1 else 0
                val vm: FollowListViewModel = hiltViewModel()
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
