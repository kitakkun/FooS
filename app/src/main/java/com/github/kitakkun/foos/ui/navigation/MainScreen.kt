package com.github.kitakkun.foos.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.kitakkun.foos.R

/**
 * メインメニューのスクリーン
 */
sealed class MainScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val iconId: Int
) {

    companion object {
        val screens = listOf(Home, Map, Reaction, Setting)
    }

    object Home : MainScreen("home", R.string.home, R.drawable.ic_home)
    object Map : MainScreen("maps", R.string.map, R.drawable.ic_pin_drop)
    object Reaction : MainScreen("reactions", R.string.reaction, R.drawable.ic_favorite)
    object Setting : MainScreen("settings", R.string.setting, R.drawable.ic_settings)
}
