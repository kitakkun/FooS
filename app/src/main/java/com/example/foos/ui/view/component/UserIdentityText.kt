package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.ui.PreviewContainer

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
        style = MaterialTheme.typography.subtitle2,
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
) {
    val usernameStyle = MaterialTheme.typography.subtitle1.copy(
        fontWeight = FontWeight.Bold,
    ).toSpanStyle()
    val userIdStyle = MaterialTheme.typography.subtitle1.copy(
        fontWeight = FontWeight.Light
    ).toSpanStyle()

    val annotatedText = remember(username, userId) {
        buildAnnotatedString {
            withStyle(usernameStyle) { append(username) }
            withStyle(userIdStyle) { append(" @$userId") }
        }
    }

    Text(text = annotatedText, modifier = modifier, overflow = TextOverflow.Ellipsis, maxLines = 1)
}

@Preview
@Composable
private fun VerticalUserIdentityTextPreview() = PreviewContainer {
    VerticalUserIdentityText(username = "username", userId = "userId")
}

@Preview
@Composable
private fun HorizontalUserIdentityTextPreview() = PreviewContainer {
    HorizontalUserIdentityText(username = "username", userId = "userId")
}
