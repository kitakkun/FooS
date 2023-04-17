package com.github.kitakkun.foos.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.customview.composable.navigation.ScreenBottomNavBar
import com.github.kitakkun.foos.customview.theme.FooSTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

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
    val screenViewModel: ScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        screenViewModel.navRoute.collect {
            navController.navigate(it) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

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
                        navController = navController,
                        onClick = { screen -> screenViewModel.navigate(screen.route) }
                    )
                }
            ) { innerPadding ->
                ScreenNavHost(
                    navController = navController,
                    screenViewModel = screenViewModel,
                    startDestination = startDestination,
                    innerPadding = innerPadding
                )
            }
        }
    }
}
