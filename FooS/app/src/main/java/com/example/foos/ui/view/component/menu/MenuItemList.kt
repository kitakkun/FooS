package com.example.foos.ui.view.component.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.constants.paddingSmall
import com.example.foos.ui.state.component.MenuItemUiState

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

