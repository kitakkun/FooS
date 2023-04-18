package com.github.kitakkun.foos.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.github.kitakkun.foos.common.navigation.BottomSheetRouter
import com.github.kitakkun.foos.navigation.mainGraph
import com.github.kitakkun.foos.post.bottomsheet.PostOptionBottomSheet
import com.github.kitakkun.foos.post.navigation.postGraph
import com.github.kitakkun.foos.user.navigation.userGraph
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

/**
 * 画面下部ナビゲーションのNavHost
 */
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        Modifier.padding(innerPadding)
    ) {
        bottomSheet(BottomSheetRouter.PostOption.routeWithArgs) {
            val postId =
                it.arguments?.getString(BottomSheetRouter.PostOption.key(0)) ?: return@bottomSheet
            PostOptionBottomSheet(navController = navController, postId = postId)
        }
        mainGraph(navController)
        postGraph(navController)
        userGraph(navController)
    }
}
