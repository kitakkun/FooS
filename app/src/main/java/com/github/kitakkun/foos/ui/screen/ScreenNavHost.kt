package com.github.kitakkun.foos.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.navigation.BottomSheet
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.post.bottomsheet.PostOptionBottomSheet
import com.github.kitakkun.foos.ui.navgraph.mainGraph
import com.github.kitakkun.foos.ui.navgraph.subGraph
import com.github.kitakkun.foos.user.auth.authGraph
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
        authGraph(navController)
        mainGraph(navController, screenViewModel)
        subGraph(navController, screenViewModel)
    }
}
