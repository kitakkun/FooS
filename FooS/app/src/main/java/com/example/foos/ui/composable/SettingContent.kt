package com.example.foos.ui.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foos.R
import com.example.foos.ui.composable.component.UserIcon
import com.example.foos.ui.theme.Yellow

@Preview(showBackground = true)
@Composable
fun SettingContent() {
    // プロフィール画像のURL
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        ClickableUserIcon(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.scale(2f)
        )
        Spacer(Modifier.height(32.dp))
        Text("Username", fontSize = 18.sp)
        Spacer(Modifier.height(60.dp))

        val menus = setOf(
            R.string.account_settings to R.drawable.ic_account_circle,
            R.string.privacy_settings to R.drawable.ic_privacy_tip,
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(menus.toList()) {
                SettingMenuItem(it.first, it.second)
            }
        }
    }
}

@Composable
fun SettingMenuItem(
    @StringRes menu: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick.invoke() }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
        Spacer(Modifier.width(16.dp))
        Text(text = stringResource(id = menu), fontSize = 18.sp)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun ClickableUserIcon(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        UserIcon(modifier = Modifier.clickable { onClick.invoke() })
        Icon(
            Icons.Filled.Edit, null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(12.dp, (-12).dp),
            tint = Yellow
        )
    }
}