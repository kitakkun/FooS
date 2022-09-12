package com.example.foos.ui.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.theme.FooSTheme

/**
 * ユーザーアイコン
 * @param url アイコン画像のURL
 * @param modifier モディファイア
 * @param onClick クリック時の処理
 */
@Composable
fun UserIcon(
    url: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .placeholder(R.drawable.ic_account_circle)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .width(50.dp)
            .height(50.dp)
            .clickable { onClick() }
    )
}

@Preview
@Composable
private fun UserIconPreview() {
    FooSTheme {
        UserIcon(url = "")
    }
}