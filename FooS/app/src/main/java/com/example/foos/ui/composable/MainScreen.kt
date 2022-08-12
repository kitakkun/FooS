package com.example.foos.ui.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foos.FirebaseMediator
import com.example.foos.R
import com.example.foos.data.posts.PostsRepository
import com.example.foos.ui.home.HomeViewModel
import com.example.foos.ui.theme.FooSTheme


sealed class Screen(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen("home", R.string.home, R.drawable.ic_home)
    object Map : Screen("maps", R.string.map, R.drawable.ic_pin_drop)
    object Reaction : Screen("reactions", R.string.reaction, R.drawable.ic_favorite)
    object Setting : Screen("settings", R.string.setting, R.drawable.ic_settings)
    object Post : Screen("post", R.string.post, R.drawable.ic_post_add)
}

@Preview
@Composable
fun MainScreen() {

    // 認証済みか確認し、未認証であれば認証を行う
    FirebaseMediator.checkSignInState(LocalContext.current)
    val navController = rememberNavController()

    FooSTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            bottomBar = { ScreenBottomNavBar(navController, ) }
        ) { innerPadding -> ScreenNavHost(navController, innerPadding) }
    }
}

@Composable
fun ScreenBottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        Screen.Home,
        Screen.Map,
        Screen.Reaction,
        Screen.Setting
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(screen.iconId), null) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ScreenNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = Screen.Home.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) { HomeContent(navController) }
        composable(Screen.Map.route) { MapContent(navController) }
        composable(Screen.Reaction.route) { ReactionContent() }
        composable(Screen.Setting.route) { SettingContent() }
        composable(Screen.Post.route) { PostContent() }
    }
}






