package com.github.kitakkun.foos.common.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class UserScreenRouter : ScreenRouter(route = "user") {

    object Auth : ScreenRouter(route = "auth") {
        object SignUp : ScreenRouter(
            route = "signup",
        )

        object SignIn : ScreenRouter(
            route = "signin",
        )
    }

    object UserProfile : ScreenRouter(
        route = "user_profile",
        arguments = listOf(navArgument("userId") { type = NavType.StringType })
    )

    object FollowList : ScreenRouter(
        route = "follow_list",
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("followees") { type = NavType.BoolType },
        )
    )
}
