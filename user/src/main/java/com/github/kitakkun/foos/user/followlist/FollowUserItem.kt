package com.github.kitakkun.foos.user.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.composable.user.VerticalUserIdentityText
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.FollowButton
import com.github.kitakkun.foos.user.R
import com.github.kitakkun.foos.user.com.github.kitakkun.foos.user.followlist.FollowUserUiState

@Composable
fun FollowUserItem(
    uiState: FollowUserUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onFollowButtonClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        if (uiState.isFollowsYouVisible) {
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
                UserIcon(url = uiState.profileImageUrl)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    VerticalUserIdentityText(
                        username = uiState.name,
                        userId = uiState.id,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = uiState.biography)
                }
            }
            if (uiState.isFollowButtonVisible) {
                Spacer(modifier = Modifier.width(16.dp))
                FollowButton(
                    onClick = onFollowButtonClicked,
                    isFollowing = uiState.isFollowedByClientUser,
                )
            }
        }
    }
}

@Preview
@Composable
fun FollowUserItemPreview() = PreviewContainer {
    FollowUserItem(
        uiState = FollowUserUiState(),
        onClick = {},
        onFollowButtonClicked = {},
    )
}
