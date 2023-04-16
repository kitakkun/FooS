package com.github.kitakkun.foos.customview.composable.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * メニューアイテムのUI状態
 * @param icon アイコンのdrawable resource
 * @param text メニューのstring resource
 * @param onClick クリック時の処理
 */
data class MenuItemUiState(
    @DrawableRes val icon: Int?,    // メニューアイコン
    @StringRes val text: Int,   // メニューテキスト
    val onClick: () -> Unit = {}    // クリックイベント時の処理
)
