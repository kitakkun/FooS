package com.example.foos.ui.view.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(menuItemUiStates) {
                MenuItem(it)
            }
        }
    }
}

/**
 * メニューアイテム
 * @param uiState UI状態
 * @param modifier モディファイア
 */
@Composable
fun MenuItem(
    uiState: MenuItemUiState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { uiState.onClick.invoke() }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // アイコン
        uiState.icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )
            Spacer(Modifier.width(16.dp))
        }
        // メニューテキスト
        Text(stringResource(id = uiState.text), fontSize = 18.sp)
    }
}
