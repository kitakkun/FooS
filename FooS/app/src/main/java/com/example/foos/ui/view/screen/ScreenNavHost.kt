package com.example.foos.ui.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrl
import com.example.foos.ui.navargs.PostItemUiStateWithImageUrlType
import com.example.foos.ui.navargs.PostType
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.screen.home.HomeScreen
import com.example.foos.ui.view.screen.home.HomeViewModel
import com.example.foos.ui.view.screen.imagedetail.ImageDetailScreen
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
        composable(Page.PostCreate.route) {
            val vm: PostViewModel = hiltViewModel()
            PostScreen(vm, navController)
        }
        composable(
            Page.UserProfile.routeWithParam,
            listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId")
            userId?.let {
                val vm: UserProfileViewModel = hiltViewModel()
                vm.setUserId(userId)
                UserProfileScreen(vm, navController)
            }
        }
        composable(
            Page.PostDetail.routeWithParam,
            listOf(navArgument("uiState") { type = PostType })
        ) {
            val uiState = it.arguments?.getParcelable<PostItemUiState>("uiState")
            val vm: PostDetailViewModel = hiltViewModel()
            uiState?.let {
                vm.setPostUiState(it)
                PostDetailScreen(vm, navController)
            }
        }
        composable(Page.ImageDetail.routeWithParam, listOf(
            navArgument("uiStateWithImageUrl") { type = PostItemUiStateWithImageUrlType }
        )
        ) {
            val uiStateWithImageUrl =
                it.arguments?.getParcelable<PostItemUiStateWithImageUrl>("uiStateWithImageUrl")
            uiStateWithImageUrl?.let {
                ImageDetailScreen(navController = navController, post = it)
            }
        }
    }
}