package com.github.kitakkun.foos.user

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.user.auth.signin.SignInScreen
import com.github.kitakkun.foos.user.auth.signup.SignUpScreen
import com.github.kitakkun.foos.user.followlist.FollowListScreen
import com.github.kitakkun.foos.user.followlist.FollowListViewModelImpl
import com.github.kitakkun.foos.user.profile.UserProfileScreen
import com.github.kitakkun.foos.user.profile.UserProfileViewModelImpl

fun NavGraphBuilder.userGraph(navController: NavController) {
    authGraph(navController)
    profileGraph(navController)
    followGraph(navController)
}

private fun NavGraphBuilder.authGraph(navController: NavController) {
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
}

private fun NavGraphBuilder.profileGraph(navController: NavController) {
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
}

private fun NavGraphBuilder.followGraph(navController: NavController) {
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
