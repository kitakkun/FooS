package com.github.kitakkun.foos.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.post.reaction.ReactionScreen
import com.github.kitakkun.foos.post.timeline.HomeScreen
import com.github.kitakkun.foos.ui.screen.map.MapScreen
import com.github.kitakkun.foos.user.setting.SettingScreen

fun NavGraphBuilder.mainGraph(
    navController: NavController,
) {
    navigation(
        route = ScreenRouter.Main.route,
        startDestination = ScreenRouter.Main.Home.route,
    ) {
        composable(ScreenRouter.Main.Home) {
        }
        composable(ScreenRouter.Main.Map) {
        }
        composable(ScreenRouter.Main.Reaction) {
        }
        composable(ScreenRouter.Main.Setting) {
        }
    }
}
