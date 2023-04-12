package com.github.kitakkun.foos.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.composable.user.VerticalUserIdentityText
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun UserItem(
    uiState: UserItemUiState,
    modifier: Modifier = Modifier,
    onItemClicked: () -> Unit,
    onFollowButtonClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(paddingMedium)
            .fillMaxWidth()
            .clickable { onItemClicked() },
    ) {
        if (uiState.followingYou) {
            Text(
                text = stringResource(R.string.follows_you),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Light
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                UserIcon(url = uiState.profileImage)
                Spacer(modifier = Modifier.width(paddingMedium))
                Column {
                    VerticalUserIdentityText(
                        username = uiState.username,
                        userId = uiState.userId,
                    )
                    Spacer(modifier = Modifier.height(paddingMedium))
                    Text(text = uiState.bio)
                }
            }
            if (uiState.userId != uiState.clientUserId) {
                FollowButton(
                    onClick = onFollowButtonClicked,
                    following = uiState.following
                )
            }
        }
    }
}

@Preview
@Composable
fun UserItemPreview() = PreviewContainer {
    val uiState = UserItemUiState(
        clientUserId = "clientUserId",
        username = "username",
        userId = "userId",
        profileImage = "",
        bio = "some interesting biography...",
        following = true,
        followingYou = true,
    )
    UserItem(uiState = uiState, onFollowButtonClicked = {}, onItemClicked = {})
}
