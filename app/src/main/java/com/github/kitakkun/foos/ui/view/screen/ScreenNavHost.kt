package com.github.kitakkun.foos.ui.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.foos.ui.navigation.BottomSheet
import com.github.kitakkun.foos.ui.navigation.MainScreen
import com.github.kitakkun.foos.ui.navigation.SubScreen
import com.github.kitakkun.foos.ui.navigation.navargs.StringList
import com.github.kitakkun.foos.ui.view.bottomsheet.PostOptionBottomSheet
import com.github.kitakkun.foos.ui.view.screen.auth.signin.SignInScreen
import com.github.kitakkun.foos.ui.view.screen.auth.signup.SignUpScreen
import com.github.kitakkun.foos.ui.view.screen.followlist.FollowListScreen
import com.github.kitakkun.foos.ui.view.screen.followlist.FollowListViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.home.HomeScreen
import com.github.kitakkun.foos.ui.view.screen.home.HomeViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.imagedetail.ImageDetailScreen
import com.github.kitakkun.foos.ui.view.screen.locationconfirm.LocationConfirmScreen
import com.github.kitakkun.foos.ui.view.screen.locationconfirm.LocationConfirmViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.locationselect.LocationSelectScreen
import com.github.kitakkun.foos.ui.view.screen.locationselect.LocationSelectViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.map.MapScreen
import com.github.kitakkun.foos.ui.view.screen.map.MapViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.post.PostScreen
import com.github.kitakkun.foos.ui.view.screen.post.PostViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.postdetail.PostDetailScreen
import com.github.kitakkun.foos.ui.view.screen.postdetail.PostDetailViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.reaction.ReactionScreen
import com.github.kitakkun.foos.ui.view.screen.reaction.ReactionViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.setting.SettingScreen
import com.github.kitakkun.foos.ui.view.screen.setting.SettingViewModelImpl
import com.github.kitakkun.foos.ui.view.screen.userprofile.UserProfileScreen
import com.github.kitakkun.foos.ui.view.screen.userprofile.UserProfileViewModelImpl
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

/**
 * 画面下部ナビゲーションのNavHost
 */
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    screenViewModel: ScreenViewModel,
    innerPadding: PaddingValues,
    startDestination: String = SubScreen.Auth.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        Modifier.padding(innerPadding)
    ) {
        bottomSheet(BottomSheet.PostOption.routeWithParam) {
            val postId =
                it.arguments?.getString(BottomSheet.PostOption.key(0)) ?: return@bottomSheet
            PostOptionBottomSheet(navController = navController, postId = postId)
        }
        navigation(
            startDestination = SubScreen.Auth.SignIn.route,
            route = SubScreen.Auth.route
        ) {
            composable(SubScreen.Auth.SignIn.route) {
                SignInScreen(viewModel = hiltViewModel(), navController = navController)
            }
            composable(SubScreen.Auth.SignUp.route) {
                SignUpScreen(viewModel = hiltViewModel(), navController = navController)
            }
        }
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
            val vm: LocationSelectViewModelImpl = hiltViewModel()
            LocationSelectScreen(vm, navController, screenViewModel)
        }
        composable(SubScreen.PostCreate.LocationConfirm.route) {
            val vm: LocationConfirmViewModelImpl = hiltViewModel()
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
