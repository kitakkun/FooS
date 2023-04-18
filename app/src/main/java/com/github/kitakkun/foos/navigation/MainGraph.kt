package com.github.kitakkun.foos.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.post.reaction.ReactionScreen
import com.github.kitakkun.foos.post.timeline.HomeScreen
import com.github.kitakkun.foos.ui.screen.map.MapScreen
import com.github.kitakkun.foos.user.setting.SettingScreen

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    navigation(
        route = ScreenRouter.Main.route,
        startDestination = ScreenRouter.Main.Home.route,
    ) {
        composable(ScreenRouter.Main.Home) {
            HomeScreen(
                navController = navController,
                screenViewModel = screenViewModel
            )
        }
        composable(ScreenRouter.Main.Map) {
            MapScreen(navController = navController)
        }
        composable(ScreenRouter.Main.Reaction) {
            ReactionScreen(navController = navController)
        }
        composable(ScreenRouter.Main.Setting) {
            SettingScreen(
                navController = navController,
                screenViewModel = screenViewModel
            )
        }
    }
}
