package com.github.kitakkun.foos.customview.composable.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun VerticalUserIdentityText(
    username: String,
    userId: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        UsernameText(username = username)
        UserIdText(userId = userId)
    }
}

@Composable
fun HorizontalUserIdentityText(
    username: String,
    userId: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        UsernameText(username = username)
        UserIdText(userId = userId)
    }
}

@Composable
private fun UsernameText(
    username: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = username,
        maxLines = 1,
        style = MaterialTheme.typography.subtitle1,
        overflow = TextOverflow.Ellipsis, /* 他のUIコンポーネントが押し出される問題の修正 */
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
        style = MaterialTheme.typography.subtitle2,
        fontWeight = FontWeight.Light,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Preview
@Composable
private fun VerticalUserIdentityTextPreview() = PreviewContainer {
    VerticalUserIdentityText(username = "username", userId = "userId")
}

@Preview
@Composable
private fun LongUsernameVerticalUserIdentityTextPreview() = PreviewContainer {
    VerticalUserIdentityText(
        username = "very long long long long long long long long long username",
        userId = "userId"
    )
}

@Preview
@Composable
private fun LongUserIdVerticalUserIdentityTextPreview() = PreviewContainer {
    VerticalUserIdentityText(
        username = "username",
        userId = "very long long long long long long long long long long long long userId"
    )
}

@Preview
@Composable
private fun HorizontalUserIdentityTextPreview() = PreviewContainer {
    HorizontalUserIdentityText(username = "username", userId = "userId")
}

@Preview
@Composable
private fun LongUsernameHorizontalUserIdentityTextPreview() = PreviewContainer {
    HorizontalUserIdentityText(
        username = "very long long long long long long long long long username",
        userId = "userId"
    )
}

@Preview
@Composable
private fun LongUserIdHorizontalUserIdentityTextPreview() = PreviewContainer {
    HorizontalUserIdentityText(
        username = "username",
        userId = "very long long long long long long long long long userId"
    )
}
