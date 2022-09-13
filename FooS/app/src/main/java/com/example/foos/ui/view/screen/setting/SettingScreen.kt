package com.example.foos.ui.view.screen.setting

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.foos.FirebaseAuthManager
import com.example.foos.R
import com.example.foos.ui.state.component.MenuItemUiState
import com.example.foos.ui.view.component.UserIcon
import com.example.foos.ui.view.component.dialog.ConfirmAlertDialog
import com.example.foos.ui.view.component.menu.MenuItemList

@Composable
fun SettingScreen(viewModel: SettingViewModel) {

    val uiState = viewModel.uiState.value
    viewModel.fetchUserData()

    val context = LocalContext.current
    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.fetchUserData()
            viewModel.onCropImageSucceeded(result)
        } else viewModel.onCropImageFailed(result)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        UserIcon(
            url = uiState.profileImage,
            onClick = {
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
                    FirebaseAuthManager.logOut()
                    (context as? Activity)?.recreate()
                },
                onDismissed = {
                    logOutRequest = false
                }
            )
        }

        MenuItemList(null, settingMenus)
    }
}
