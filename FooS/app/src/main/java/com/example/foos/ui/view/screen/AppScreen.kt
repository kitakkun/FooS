package com.example.foos.ui.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.navigation.ScreenBottomNavBar
import com.example.foos.ui.view.component.navigation.ScreenTopBar

/**
 * アプリ画面のコンポーザブル（アプリ全体のエントリポイント）
 */
@Preview
@Composable
fun AppScreen() {

    val navController = rememberNavController()
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
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                ScreenTopBar(navController)
            },
            bottomBar = {
                ScreenBottomNavBar(
                    navController,
                    onClick = { screen -> screenViewModel.navigate(screen.route) })
            }
        ) { innerPadding -> ScreenNavHost(navController, screenViewModel, innerPadding) }
    }
}
