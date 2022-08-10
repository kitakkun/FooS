package com.example.foos.ui.composable.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foos.R

// メニューアイテム
@Composable
fun MenuItemsRow(
    @StringRes headerText: Int?,
    menuItems: List<MenuItem>,
) {
    headerText?.let {
        Text(stringResource(id = it))
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(menuItems) {
            MenuItemRow(it)
        }
    }
}

@Preview
@Composable
fun MenuItemRow(
    menuItem: MenuItem = MenuItem(R.string.account_settings, R.drawable.ic_account_circle),
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { menuItem.onClick.invoke() }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // アイコン
        menuItem.icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )
            Spacer(Modifier.width(16.dp))
        }
        // メニューテキスト
        Text(stringResource(id = menuItem.menuText), fontSize = 18.sp)
    }
}

// メニューアイテム
data class MenuItem(
    @StringRes val menuText: Int,   // メニューテキスト
    @DrawableRes val icon: Int?,    // メニューアイコン
    val onClick: () -> Unit = {}    // クリックイベント時の処理
)