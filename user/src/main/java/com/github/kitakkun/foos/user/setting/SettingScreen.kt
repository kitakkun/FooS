package com.github.kitakkun.foos.user.setting

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.github.kitakkun.foos.common.ext.navigateToSingleScreen
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.customview.composable.dialog.ConfirmAlertDialog
import com.github.kitakkun.foos.customview.composable.menu.MenuItemList
import com.github.kitakkun.foos.customview.composable.menu.MenuItemUiState
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.user.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Destination(start = true)
@Composable
fun SettingScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel: SettingViewModel = koinViewModel()
    val navController: NavController = get()
    val uiState by viewModel.uiState.collectAsState()

    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.fetchUserData()
            viewModel.onCropImageSucceeded(result)
        } else viewModel.onCropImageFailed(result)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    SettingUI(
        uiState = uiState,
        onUserIconClick = {
            cropImage.launch(
                options {
                    setCropShape(CropImageView.CropShape.RECTANGLE)
                    setAspectRatio(1, 1)
                    setFixAspectRatio(true)
                    setImageSource(includeGallery = true, includeCamera = true)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    setRequestedSize(64, 64, CropImageView.RequestSizeOptions.SAMPLING)
                }
            )
        },
        onLogOut = {
            viewModel.logOut()
            navController.navigateToSingleScreen(UserScreenRouter.Auth)
        }
    )
}

@Composable
private fun SettingUI(
    uiState: SettingUiState,
    onUserIconClick: () -> Unit,
    onLogOut: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        UserIcon(
            url = uiState.profileImage,
            onClick = onUserIconClick,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(32.dp))
        Text(uiState.username, fontSize = 18.sp)
        Spacer(Modifier.height(60.dp))
        var logOutRequest by remember { mutableStateOf(false) }

        val settingMenus = listOf(
            MenuItemUiState(R.drawable.ic_account_circle, R.string.account_settings),
            MenuItemUiState(R.drawable.ic_privacy_tip, R.string.privacy_settings),
            MenuItemUiState(
                R.drawable.ic_log_out,
                R.string.log_out,
                onClick = { logOutRequest = true }),
        )

        if (logOutRequest) {
            ConfirmAlertDialog(
                title = stringResource(id = R.string.log_out_confirm_title),
                message = stringResource(id = R.string.log_out_confirm_message),
                confirmButtonText = stringResource(id = R.string.dialog_ok),
                dismissButtonText = stringResource(id = R.string.dialog_cancel),
                onConfirmed = {
                    logOutRequest = false
                    onLogOut()
                },
                onDismissed = { logOutRequest = false }
            )
        }

        MenuItemList(null, settingMenus)
    }
}

@Preview
@Composable
private fun SettingUIPreview() = PreviewContainer {
    SettingUI(
        uiState = SettingUiState.Default.copy(username = "username"),
        onUserIconClick = {},
        onLogOut = {}
    )
}
