package com.github.kitakkun.foos.common.ext

import androidx.navigation.NavController
import com.github.kitakkun.foos.common.navigation.ScreenRouter

fun NavController.navigateToSingleScreen(screenRouter: ScreenRouter) {
    navigate(screenRouter.route) {
        popUpTo(screenRouter.route) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
