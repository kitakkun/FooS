package com.example.foos.ui.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium

/**
 * フォロー情報
 * @param followerNum フォロワー数
 * @param followeeNum フォロー数
 * @param onFollowersTextClick フォロワー数テキストクリック時の処理
 * @param onFolloweesTextClick フォロー数テキストクリック時の処理
 */
@Composable
fun FollowInfo(
    followerNum: Int,
    followeeNum: Int,
    onFollowersTextClick: () -> Unit = {},
    onFollowingTextClick: () -> Unit = {},
) {
    Row {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$followeeNum ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)) {
                    append(stringResource(id = R.string.following))
                }
            },
            modifier = Modifier
                .padding(paddingMedium)
                .clickable { onFollowingTextClick() }
        )
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$followerNum ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)) {
                    append(stringResource(id = R.string.followers))
                }
            },
            modifier = Modifier
                .padding(paddingMedium)
                .clickable { onFollowersTextClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowInfoPreview() {
    FollowInfo(followeeNum = 10, followerNum = 20)
}
