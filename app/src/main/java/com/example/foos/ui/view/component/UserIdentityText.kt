package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.ui.constants.paddingSmall
import com.example.foos.ui.theme.FooSTheme

@Composable
private fun UsernameText(
    username: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = username,
        maxLines = 1,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun UserIdText(
    userId: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "@${userId}",
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Light,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

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
        UsernameText(username = username)
        UserIdText(userId = userId)
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
        UsernameText(username = username)
        Spacer(Modifier.width(paddingSmall))
        UserIdText(userId = userId, modifier = Modifier.weight(1f, false))
        after()
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalUserIdentityTextPreview() {
    FooSTheme {
        VerticalUserIdentityText(username = "username", userId = "userId")
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalUserIdentityTextPreview() {
    FooSTheme {
        HorizontalUserIdentityText(username = "username", userId = "userId")
    }
}
