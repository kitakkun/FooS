package com.example.foos.ui.view.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foos.R
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.view.component.FollowButton
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.component.VerticalUserIdentityText

@Composable
fun UserItem(
    uiState: UserItemUiState,
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit = {},
    onFollowButtonClicked: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(uiState.userId) },
    ) {
        if (uiState.followingYou) {
            Text(text = stringResource(R.string.follows_you), fontWeight = FontWeight.Light)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                UserIcon(url = uiState.profileImage)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row {
                        VerticalUserIdentityText(
                            username = uiState.username,
                            userId = uiState.userId,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = uiState.bio)
                }
            }
            if (uiState.userId != uiState.clientUserId) {
                FollowButton(onClick = { onFollowButtonClicked(uiState.userId) }, following = uiState.following)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    val uiState = UserItemUiState(
        clientUserId = "userId",
        username = "username",
        userId = "userId",
        profileImage = "",
        bio = "some interesting biography...",
        following = true,
        followingYou = true,
    )
    UserItem(uiState = uiState)
}