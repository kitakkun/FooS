package com.github.kitakkun.foos.post.create

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmScreen
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmViewModelImpl
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectScreen
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectViewModelImpl

fun NavGraphBuilder.postCreateGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    navigation(
        route = SubScreen.PostCreate.route,
        startDestination = SubScreen.PostCreate.Edit.route,
    ) {
        composable(SubScreen.PostCreate.Edit.route) {
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
    }
}
