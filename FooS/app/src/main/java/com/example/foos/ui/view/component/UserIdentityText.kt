package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun VerticalUserIdentityText(
    username: String,
    userId: String,
    modifier: Modifier = Modifier,
    before: @Composable () -> Unit = {},
    after: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        before()
        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
        Text(
            text = "@${userId}",
            fontWeight = FontWeight.Light,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        after()
    }
}

@Composable
fun HorizontalUserIdentityText(
    username: String,
    userId: String,
    modifier: Modifier = Modifier,
    before: @Composable () -> Unit = {},
    after: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        before()
        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
        Text(
            text = "@${userId}",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        after()
    }
}
