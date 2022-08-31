package com.example.foos.ui.view.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foos.FirebaseAuthManager
import com.example.foos.ui.navigation.Screen
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.theme.FooSTheme

/**
 * メイン画面のコンポーザブル（アプリ全体のエントリポイント）
 */
@Preview
@Composable
fun MainScreen() {

    // 認証済みか確認し、未認証であれば認証を行う
    FirebaseAuthManager.checkSignInState(LocalContext.current)
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
fun RowScope.MyBottomNavigationItem(
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