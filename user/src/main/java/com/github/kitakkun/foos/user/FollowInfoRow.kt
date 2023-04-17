package com.github.kitakkun.foos.user

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun FollowInfoRow(
    followCount: Int,
    followerCount: Int,
    onFollowingTextClick: () -> Unit,
    onFollowersTextClick: () -> Unit,
) {
    val numberStyle = SpanStyle(fontWeight = FontWeight.Bold)
    val wordStyle = SpanStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)

    val followingText = buildAnnotatedString {
        withStyle(numberStyle) {
            append("$followerCount ")
        }
        withStyle(wordStyle) {
            append(stringResource(id = R.string.following))
        }
    }

    val followersText = buildAnnotatedString {
        withStyle(numberStyle) {
            append("$followCount ")
        }
        withStyle(wordStyle) {
            append(stringResource(id = R.string.followers))
        }
    }

    Row(
        modifier = Modifier.padding(16.dp),
    ) {
        ClickableText(
            text = followingText,
            onClick = { onFollowingTextClick() },
        )
        Spacer(modifier = Modifier.width(16.dp))
        ClickableText(
            text = followersText,
            onClick = { onFollowersTextClick() },
        )
    }
}

@Preview
@Composable
private fun FollowInfoPreview() = PreviewContainer {
    FollowInfoRow(
        followerCount = 10,
        followCount = 20,
        onFollowingTextClick = {},
        onFollowersTextClick = {},
    )
}
