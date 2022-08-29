package com.example.foos.ui.view.screen.followlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foos.R
import com.example.foos.ui.state.screen.followlist.UserItemUiState
import com.example.foos.ui.view.component.RoundButton
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.component.VerticalUserIdentityText

@Composable
fun FollowListScreen(viewModel: FollowListViewModel, userId: String) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFollowers(userId)
    }

    UserList(uiStates = uiState.value.users)
}

@Composable
fun UserList(
    uiStates: List<UserItemUiState>
) {
    LazyColumn() {
        items(uiStates) {
            UserItem(uiState = it)
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun UserItem(
    uiState: UserItemUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        if (uiState.followingYou) {
            Text(text = "follows you", fontWeight = FontWeight.Light)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
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
                            after = {

                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = uiState.bio)
                }
            }
            RoundButton(onClick = { /*TODO*/ }) {
                if (uiState.following) {
                    Text(text = stringResource(id = R.string.following))
                } else {
                    Text(text = stringResource(id = R.string.follow))
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserItemPreview() {
    val uiState = UserItemUiState("username", "userId", "", "BIO", true, false)
    UserItem(uiState = uiState)
}