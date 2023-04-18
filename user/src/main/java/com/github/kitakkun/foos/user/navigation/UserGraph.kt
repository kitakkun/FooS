package com.github.kitakkun.foos.user.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
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
        route = UserScreenRouter.Auth.route,
        startDestination = UserScreenRouter.Auth.SignIn.route,
    ) {
        composable(UserScreenRouter.Auth.SignIn) {
            SignInScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable(UserScreenRouter.Auth.SignUp) {
            SignUpScreen(viewModel = hiltViewModel(), navController = navController)
        }
    }
}

private fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(UserScreenRouter.UserProfile) {
        val arguments = UserScreenRouter.UserProfile.resolveArguments(it)
        val userId = arguments[0] as String? ?: return@composable
        val vm: UserProfileViewModelImpl = hiltViewModel()
        UserProfileScreen(vm, navController, userId)
    }
}

private fun NavGraphBuilder.followGraph(navController: NavController) {
    composable(UserScreenRouter.FollowList) {
        val arguments = UserScreenRouter.FollowList.resolveArguments(it)
        val userId = arguments[0] as String?
        val followees = arguments[1] as Boolean?
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
