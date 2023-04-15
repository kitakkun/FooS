package com.github.kitakkun.foos.ui.view.component.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.common.const.paddingSmall
import com.github.kitakkun.foos.ui.state.component.MenuItemUiState
import com.github.kitakkun.foos.user.setting.MenuItem

/**
 * メニューリスト
 * @param headerText メニューの見出し
 * @param menuItemUiStates 各メニューアイテムのUI状態
 */
@Composable
fun MenuItemList(
    @StringRes headerText: Int?,
    menuItemUiStates: List<MenuItemUiState>,
) {
    Column {
        headerText?.let { Text(stringResource(it)) }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(paddingMedium),
            contentPadding = PaddingValues(vertical = paddingSmall)
        ) {
            items(menuItemUiStates) {
                MenuItem(it)
            }
        }
    }
}
