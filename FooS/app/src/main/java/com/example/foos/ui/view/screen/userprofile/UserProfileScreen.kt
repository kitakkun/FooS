package com.example.foos.ui.view.screen.userprofile

import android.hardware.lights.Light
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.screen.Page

@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()

    Column() {
        UserProfile(uiState.value.username, uiState.value.userId, uiState.value.userIcon)
    }
}

/**
 * ユーザプロフィール
 * 名前、プロフ画像、ID
 */
@Composable
fun UserProfile(
    username: String,
    userId: String,
    profileImage: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserIcon(url = profileImage)
        Text( username, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text( userId, fontWeight = FontWeight.Light, fontSize = 12.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserProfilePreview() {
    UserProfile("username", "userId", "")
}