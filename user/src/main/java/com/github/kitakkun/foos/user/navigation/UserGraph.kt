package com.github.kitakkun.foos.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.user.auth.signin.SignInScreen
import com.github.kitakkun.foos.user.auth.signup.SignUpScreen
import com.github.kitakkun.foos.user.followlist.FollowListScreen
import com.github.kitakkun.foos.user.profile.UserProfileScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
            SignInScreen(viewModel = koinViewModel(), navController = navController)
        }
        composable(UserScreenRouter.Auth.SignUp) {
            SignUpScreen(viewModel = koinViewModel(), navController = navController)
        }
    }
}

private fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(UserScreenRouter.UserProfile) {
        val arguments = UserScreenRouter.UserProfile.resolveArguments(it)
        val userId = arguments[0] as String? ?: return@composable
        UserProfileScreen(
            viewModel = koinViewModel { parametersOf(userId) },
            navController = navController
        )
    }
}

private fun NavGraphBuilder.followGraph(navController: NavController) {
    composable(UserScreenRouter.FollowList) {
        val arguments = UserScreenRouter.FollowList.resolveArguments(it)
        val userId = arguments[0] as String? ?: return@composable
        val shouldShowFollowingListFirst = arguments[1] as Boolean? ?: return@composable
        FollowListScreen(
            viewModel = koinViewModel { parametersOf(userId, shouldShowFollowingListFirst) },
            navController = navController
        )
    }
}
