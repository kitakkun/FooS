package com.github.kitakkun.foos.user.auth

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.user.auth.signin.SignInScreen
import com.github.kitakkun.foos.user.auth.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) =
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
