package com.example.foos.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.foos.R

/**
 * メインメニューのスクリーン
 */
sealed class Screen(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen("home", R.string.home, R.drawable.ic_home)
    object Map : Screen("maps", R.string.map, R.drawable.ic_pin_drop)
    object Reaction : Screen("reactions", R.string.reaction, R.drawable.ic_favorite)
    object Setting : Screen("settings", R.string.setting, R.drawable.ic_settings)
}

