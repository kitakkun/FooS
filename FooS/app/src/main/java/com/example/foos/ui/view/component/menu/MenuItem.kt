package com.example.foos.ui.view.component.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.state.component.MenuItemUiState

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
            .padding(paddingMedium)
    ) {
        // アイコン
        uiState.icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )
            Spacer(Modifier.width(paddingMedium))
        }
        // メニューテキスト
        Text(stringResource(id = uiState.text), fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuItemPreview() {
    val uiState = MenuItemUiState(
        icon = R.drawable.ic_account_circle,
        text = R.string.account_settings
    )
    MenuItem(uiState = uiState)
}