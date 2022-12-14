package com.example.foos.ui.view.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.button.FollowButton
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.component.VerticalUserIdentityText

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
                    Row {
                        VerticalUserIdentityText(
                            username = uiState.username,
                            userId = uiState.userId,
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    val uiState = UserItemUiState(
        clientUserId = "clientUserId",
        username = "username",
        userId = "userId",
        profileImage = "",
        bio = "some interesting biography...",
        following = true,
        followingYou = true,
    )
    FooSTheme {
        UserItem(uiState = uiState, onFollowButtonClicked = {}, onItemClicked = {})
    }
}
