package com.example.foos.ui.view.component.button

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.R
import com.example.foos.ui.theme.FooSTheme

/**
 * フォローボタン
 * @param following フォロー状態
 * @param onClick ボタンクリック時のイベント
 */
@Composable
fun FollowButton(
    following: Boolean,
    onClick: () -> Unit = {},
) {
    val text =
        if (following) stringResource(id = R.string.following)
        else stringResource(id = R.string.follow)
    RoundButton(onClick = onClick, outlined = following) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun FollowButtonPreview() {
    val followingFlag = remember {
        mutableStateOf(false)
    }
    FooSTheme {
        FollowButton(
            following = followingFlag.value,
            onClick = { followingFlag.value = !followingFlag.value }
        )
    }
}