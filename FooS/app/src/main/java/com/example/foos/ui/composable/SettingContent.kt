package com.example.foos.ui.composable

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.foos.FirebaseMediator
import com.example.foos.R
import com.example.foos.ui.composable.component.AsyncUserIcon
import com.example.foos.ui.composable.component.ConfirmAlertDialog
import com.example.foos.ui.composable.component.MenuItem
import com.example.foos.ui.composable.component.MenuItemsRow
import com.example.foos.ui.theme.Yellow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun SettingContent() {
    val context = LocalContext.current
    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) onCropImageSuccessful(result)
        else onCropImageFail(result)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        ClickableUserIcon(
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
        Firebase.auth.currentUser?.displayName?.let { Text(it, fontSize = 18.sp) }
        Spacer(Modifier.height(60.dp))

        var logOutRequest by remember { mutableStateOf(false) }

        val settingMenus = listOf(
            MenuItem(R.string.account_settings, R.drawable.ic_account_circle),
            MenuItem(R.string.privacy_settings, R.drawable.ic_privacy_tip),
            MenuItem(R.string.log_out, R.drawable.ic_log_out, onClick = { logOutRequest = true }),
        )

        if (logOutRequest) {
            ConfirmAlertDialog(
                title = stringResource(id = R.string.log_out_confirm_title),
                message = stringResource(id = R.string.log_out_confirm_message),
                onConfirmed = {
                    FirebaseMediator.logOut()
                    (context as? Activity)?.recreate()
                },
                onDismissed = {
                    logOutRequest = false
                }
            )
        }

        MenuItemsRow(null, settingMenus)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun ClickableUserIcon(
    onClick: () -> Unit = {},
    iconScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    var url by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        if (url == "") {
            val ref =
                Firebase.firestore.collection("userinfo").document(Firebase.auth.uid.toString())
            ref.get().addOnSuccessListener {
                url = it.get("profileImagePath").toString()
            }
        }
        AsyncUserIcon(
            url = url,
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(1.dp, Color.Gray)
                .clickable { onClick.invoke() })
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