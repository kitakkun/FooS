package com.github.kitakkun.foos.customview.composable.navigation

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
import com.github.kitakkun.foos.common.navigation.MainScreen

/**
 * 画面下部のナビゲーションバー
 */
@Composable
fun ScreenBottomNavBar(
    navController: NavHostController,
    onClick: (MainScreen) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (MainScreen.screens.map { it.route }.contains(currentDestination?.route)) {
        BottomNavigation {
            MainScreen.screens.forEach { screen ->
                MyBottomNavigationItem(
                    mainScreen = screen,
                    onClick = onClick,
                    currentDestination = currentDestination,
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
    mainScreen: MainScreen,
    currentDestination: NavDestination?,
    onClick: (MainScreen) -> Unit,
) {
    BottomNavigationItem(
        label = { Text(text = stringResource(id = mainScreen.stringId)) },
        icon = { Icon(painterResource(mainScreen.iconId), null) },
        selected = currentDestination?.hierarchy?.any { it.route == mainScreen.route } == true,
        onClick = { onClick.invoke(mainScreen) }
    )
}
