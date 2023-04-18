package com.github.kitakkun.foos.common.ext

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.kitakkun.foos.common.navigation.ScreenRouter

fun NavGraphBuilder.composable(
    screenRouter: ScreenRouter,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    this.composable(
        route = screenRouter.routeWithArgs,
        arguments = screenRouter.arguments,
        deepLinks = screenRouter.deepLinks,
        content = content,
    )
}
