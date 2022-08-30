package com.example.foos.ui.view.screen.setting

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.foos.ui.theme.Yellow
import com.example.foos.ui.view.component.ConfirmAlertDialog
import com.example.foos.ui.view.component.MenuItemList
import com.example.foos.ui.view.component.UserIcon
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun SettingScreen(viewModel: SettingViewModel) {

    val uiState = viewModel.uiState.value
    viewModel.fetchUserData()

    val context = LocalContext.current
    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.fetchUserData()
            viewModel.onCropImageSuccessful(result)
        } else onCropImageFail(result)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        ClickableUserIcon(
            imageUri = uiState.profileImage,
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
            iconScale = 2f
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

@Composable
fun ClickableUserIcon(
    imageUri: String,
    onClick: () -> Unit = {},
    iconScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        UserIcon(
            url = imageUri,
            onClick = onClick,
            modifier = Modifier.width(70.dp).height(70.dp)
        )
        Icon(
            Icons.Filled.Edit, null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(12.dp, (-12).dp),
            tint = Yellow
        )
    }
}

fun onCropImageSuccessful(result: CropImageView.CropResult) {
    val uriContent = result.uriContent
    val ref = Firebase.storage.reference.child("images/profile/" + Firebase.auth.uid + ".png")
    uriContent?.let {
        val uploadTask = ref.putFile(uriContent)
        uploadTask.addOnFailureListener {
            Log.d("MAIN_ACTIVITY", "UPLOAD FAILED")
        }.addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val map = mapOf("profileImagePath" to it.toString())
                val refLink = Firebase.firestore.collection("userinfo")
                    .document(Firebase.auth.uid.toString()).set(map)
            }
            Log.d("MAIN_ACTIVITY", "SUCCESS")
        }
    }
}

fun onCropImageFail(result: CropImageView.CropResult) {
    val exception = result.error
}