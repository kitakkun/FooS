package com.example.foos.ui.view.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foos.FirebaseMediator
import com.example.foos.R
import com.example.foos.ui.navargs.Post
import com.example.foos.ui.navargs.PostType
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.view.screen.home.HomeScreen
import com.example.foos.ui.view.screen.home.HomeViewModel
import com.example.foos.ui.view.screen.imagedetail.ImageDetailScreen
import com.example.foos.ui.view.screen.map.MapScreen
import com.example.foos.ui.view.screen.map.MapViewModel
import com.example.foos.ui.view.screen.post.PostScreen
import com.example.foos.ui.view.screen.post.PostViewModel
import com.example.foos.ui.view.screen.postdetail.PostDetailScreen
import com.example.foos.ui.view.screen.postdetail.PostDetailViewModel
import com.example.foos.ui.view.screen.reaction.ReactionScreen
import com.example.foos.ui.view.screen.reaction.ReactionViewModel
import com.example.foos.ui.view.screen.setting.SettingScreen
import com.example.foos.ui.view.screen.setting.SettingViewModel
import com.example.foos.ui.theme.FooSTheme


sealed class Screen(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen("home", R.string.home, R.drawable.ic_home)
    object Map : Screen("maps", R.string.map, R.drawable.ic_pin_drop)
    object Reaction : Screen("reactions", R.string.reaction, R.drawable.ic_favorite)
    object Setting : Screen("settings", R.string.setting, R.drawable.ic_settings)
}

sealed class Page(val route: String) {
    object PostDetail : Page("post_detail")
    object ImageDetail : Page("image_detail")
    object Post : Page("post")
}

@Preview
@Composable
fun MainScreen() {

    // 認証済みか確認し、未認証であれば認証を行う
    FirebaseMediator.checkSignInState(LocalContext.current)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = rememberSaveable {
        mutableStateOf(true)
    }

    showBottomBar.value = when (currentDestination?.route) {
        Page.Post.route -> false
        Page.PostDetail.route -> false
        else -> true
    }

    FooSTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            bottomBar = { if (showBottomBar.value) ScreenBottomNavBar(navController) }
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
        composable(Screen.Home.route) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(vm, navController)
        }
        composable(Screen.Map.route) {
            val vm: MapViewModel = hiltViewModel()
            MapScreen(vm, navController)
        }
        composable(Screen.Reaction.route) {
            val vm: ReactionViewModel = hiltViewModel()
            ReactionScreen(vm, navController)
        }
        composable(Screen.Setting.route) {
            val vm: SettingViewModel = hiltViewModel()
            SettingScreen(vm)
        }
        composable(Page.Post.route) {
            val vm: PostViewModel = hiltViewModel()
            PostScreen(vm, navController)
        }
        composable(
            "${Page.PostDetail.route}/{uiState}",
            listOf(navArgument("uiState") { type = PostType })
        ) {
            val uiState = it.arguments?.getParcelable<PostItemUiState>("uiState")
            val vm: PostDetailViewModel = hiltViewModel()
            uiState?.let {
                vm.setPostUiState(it)
                PostDetailScreen(vm, navController)
            }
        }
        composable("${Page.ImageDetail.route}/{post}", listOf(
            navArgument("post") { type = PostType }
        )
        ) {
            val post = it.arguments?.getParcelable<Post>("post")
            ImageDetailScreen(navController = navController, post = post)
        }
    }
}






