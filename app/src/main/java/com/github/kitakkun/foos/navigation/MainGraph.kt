package com.github.kitakkun.foos.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.navigation.MainScreen
import com.github.kitakkun.foos.post.reaction.ReactionScreen
import com.github.kitakkun.foos.post.reaction.ReactionViewModelImpl
import com.github.kitakkun.foos.post.timeline.HomeScreen
import com.github.kitakkun.foos.post.timeline.HomeViewModelImpl
import com.github.kitakkun.foos.ui.screen.map.MapScreen
import com.github.kitakkun.foos.ui.screen.map.MapViewModelImpl
import com.github.kitakkun.foos.user.setting.SettingScreen
import com.github.kitakkun.foos.user.setting.SettingViewModelImpl

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
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
}
