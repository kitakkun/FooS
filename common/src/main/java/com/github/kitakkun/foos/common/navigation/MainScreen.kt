package com.github.kitakkun.foos.common.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.kitakkun.foos.common.R

/**
 * メインメニューのスクリーン
 */
sealed class MainScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val iconId: Int
) {

    companion object {
        // need by lazy to avoid null pointer exception
        // references:
        // - https://stackoverflow.com/questions/54940944/initializing-companion-object-after-inner-objects/55010004
        // - https://youtrack.jetbrains.com/issue/KT-8970/Object-is-uninitialized-null-when-accessed-from-static-context-ex.-companion-object-with-initialization-loop
        val screens by lazy { listOf(Home, Map, Reaction, Setting) }
    }

    object Home : MainScreen(
        "home",
        R.string.home,
        R.drawable.ic_home
    )

    object Map : MainScreen(
        "maps",
        R.string.map,
        R.drawable.ic_pin_drop
    )

    object Reaction : MainScreen(
        "reactions",
        R.string.reaction,
        R.drawable.ic_favorite
    )

    object Setting : MainScreen(
        "settings",
        R.string.setting,
        R.drawable.ic_settings
    )
}
