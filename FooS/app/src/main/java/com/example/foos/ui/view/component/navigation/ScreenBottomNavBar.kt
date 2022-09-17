package com.example.foos.ui.view.component.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foos.ui.navigation.Screen

/**
 * 画面下部のナビゲーションバー
 */
@Composable
fun ScreenBottomNavBar(
    navController: NavHostController,
    onClick: (Screen) -> Unit
) {
    val screens = listOf(
        Screen.Home,
        Screen.Map,
        Screen.Reaction,
        Screen.Setting
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (screens.map { it.route }.contains(currentDestination?.route)) {
        BottomNavigation {
            screens.forEach { screen ->
                MyBottomNavigationItem(
                    screen = screen,
                    onClick = onClick,
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}

/**
 * 画面下部ナビゲーションバーの項目
 */
@Composable
private fun RowScope.MyBottomNavigationItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    onClick: (Screen) -> Unit,
) {
    BottomNavigationItem(
        label = { Text(text = stringResource(id = screen.stringId)) },
        icon = { Icon(painterResource(screen.iconId), null) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = { onClick.invoke(screen) }
    )
}
