package com.example.foos.ui.view.component.navigation

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foos.ui.navigation.SubScreen

/**
 * 画面トップナビゲーションバー
 */
@Composable
fun ScreenTopBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val subScreens = listOf(
        SubScreen.ImageDetail,
        SubScreen.UserProfile,
        SubScreen.PostDetail,
    )
    if (subScreens.map { it.routeWithParam }.contains(currentDestination?.route)) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {},
        )
    }
}
