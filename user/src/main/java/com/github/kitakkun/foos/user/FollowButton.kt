package com.github.kitakkun.foos.user

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.composable.button.RoundButton
import com.github.kitakkun.foos.customview.preview.PreviewContainer

/**
 * フォローボタン
 * @param isFollowing フォロー状態
 * @param onClick ボタンクリック時のイベント
 */
@Composable
fun FollowButton(
    isFollowing: Boolean,
    onClick: () -> Unit = {},
) {
    RoundButton(
        onClick = onClick,
        outlined = isFollowing
    ) {
        Text(
            text = when (isFollowing) {
                true -> stringResource(id = R.string.following)
                false -> stringResource(id = R.string.follow)
            }
        )
    }
}

@Preview
@Composable
private fun FollowButtonPreview() = PreviewContainer {
    FollowButton(
        isFollowing = false,
        onClick = {}
    )
}

@Preview
@Composable
private fun FollowingButtonPreview() = PreviewContainer {
    FollowButton(
        isFollowing = true,
        onClick = {}
    )
}
