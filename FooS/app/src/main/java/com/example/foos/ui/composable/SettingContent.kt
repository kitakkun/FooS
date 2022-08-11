package com.example.foos.ui.composable

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.foos.FirebaseMediator
import com.example.foos.R
import com.example.foos.ui.composable.component.*
import com.example.foos.ui.theme.Yellow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream

@Preview(showBackground = true)
@Composable
fun SettingContent() {
    val context = LocalContext.current
    // プロフィール画像のURL
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent

            val ref = Firebase.storage.reference.child("images/profile/"+ Firebase.auth.uid + ".png")
            uriContent?.let {
                val uploadTask = ref.putFile(uriContent)
                uploadTask.addOnFailureListener {
                    Log.d("MAIN_ACTIVITY", "UPLOAD FAILED")
                }.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        val refLink = Firebase.firestore
                            .document("userinfo/userprofile").set(it.toString() )
                    }
                    Log.d("MAIN_ACTIVITY", "SUCCESS")
                }
            }
        } else {
            val exception = result.error
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        ClickableUserIcon(
            onClick = {
                cropImage.launch (
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
        Text("Username", fontSize = 18.sp)
        Spacer(Modifier.height(60.dp))

        var logOutRequest by remember { mutableStateOf(false) }

        val settingMenus = listOf(
            MenuItem(R.string.account_settings, R.drawable.ic_account_circle),
            MenuItem(R.string.privacy_settings, R.drawable.ic_privacy_tip),
            MenuItem(R.string.log_out, R.drawable.ic_log_out, onClick = { logOutRequest = true }),
        )

        Log.d("SETTINGS", logOutRequest.toString())

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
    Box(modifier = modifier) {
        AsyncUserIcon(
            url = "gs://foos-bdebd.appspot.com/images/profile/user.png",
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