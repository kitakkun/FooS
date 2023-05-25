package com.github.kitakkun.foos.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.kitakkun.foos.common.ext.navigateToSingleScreen
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.customview.composable.navigation.ScreenBottomNavBar
import com.github.kitakkun.foos.customview.theme.FooSTheme
import com.github.kitakkun.foos.user.setting.NavGraphs
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost

/**
 * アプリ画面のコンポーザブル（アプリ全体のエントリポイント）
 */
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun AppScreen(
    startDestination: String,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    FooSTheme {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            scrimColor = Color.Black.copy(alpha = 0.4f)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = MaterialTheme.colors.background,
                bottomBar = {
                    ScreenBottomNavBar(
                        isVisible = navBackStackEntry?.destination?.route in ScreenRouter.Main.routes,
                        currentRoute = navBackStackEntry?.destination?.route,
                        onHomeClick = {
                            navController.navigateToSingleScreen(ScreenRouter.Main.Home)
                        },
                        onMapsClick = {
                            navController.navigateToSingleScreen(ScreenRouter.Main.Map)
                        },
                        onSettingsClick = {
                            navController.navigateToSingleScreen(ScreenRouter.Main.Setting)
                        },
                        onReactionsClick = {
                            navController.navigateToSingleScreen(ScreenRouter.Main.Reaction)
                        },
                    )
                }
            ) { innerPadding ->
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
