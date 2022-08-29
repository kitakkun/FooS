package com.example.foos.ui.view.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foos.FirebaseAuthManager
import com.example.foos.R
import com.example.foos.ui.theme.FooSTheme

/**
 * メインメニューのスクリーン
 */
sealed class Screen(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen("home", R.string.home, R.drawable.ic_home)
    object Map : Screen("maps", R.string.map, R.drawable.ic_pin_drop)
    object Reaction : Screen("reactions", R.string.reaction, R.drawable.ic_favorite)
    object Setting : Screen("settings", R.string.setting, R.drawable.ic_settings)
}

/**
 * サブスクリーン（メインメニューのスクリーンから呼ばれる）
 */
sealed class Page(val route: String, val routeWithParam: String = "") {
    object PostDetail : Page("post_detail", "post_detail/{uiState}")
    object ImageDetail : Page("image_detail", "image_detail/{uiStateWithImageUrl}")
    object PostCreate : Page("post_create")
    object UserProfile : Page("user_profile", "user_profile/{userId}")
    object FollowList: Page("follow_list", "follow_list/{userId}/{followees}")
    object LocationSelect: Page("location_select")
    object LocationConfirm: Page("location_confirm", "location_confirm/{longitude}/{latitude}")
}

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
        icon = { Icon(painterResource(screen.iconId), null) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = { onClick.invoke(screen) }
    )
}
