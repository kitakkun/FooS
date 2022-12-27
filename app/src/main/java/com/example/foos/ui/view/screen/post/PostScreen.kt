package com.example.foos.ui.view.screen.post

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.ImageAttachment
import com.example.foos.ui.view.component.LocationAttachment
import com.example.foos.ui.view.screen.ScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

/**
 * 投稿画面のコンポーザブル
 * テキスト、画像、位置情報を指定することが可能。
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostScreen(
    viewModel: PostViewModelImpl,
    navController: NavController,
    sharedViewModel: ScreenViewModel,
) {

    val location = sharedViewModel.postCreateSharedData.value.location
    val locationName = sharedViewModel.postCreateSharedData.value.locationName

    val locationAttached = location != null && locationName != null
    Log.d("LOCATION", locationAttached.toString())

    if (locationAttached) {
        viewModel.applyLocationData(location!!, locationName!!)
    }

    LaunchedEffect(Unit) {
        launch {
            viewModel.navEvent.collect {
                navController.navigate(it)
            }
        }
        launch {
            viewModel.navUpEvent.collect {
                navController.navigateUp()
            }
        }
    }

    val uiState = viewModel.uiState.value

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) {
        viewModel.setImages(context, it)
    }

    val filePermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    PostUI(
        uiState = uiState,
        onImageAttachmentRemove = { viewModel.onImageAttachmentRemove(it) },
        onAddImage = {
            if (filePermissionState.status == PermissionStatus.Granted) {
                launcher.launch("image/*")
            } else {
                filePermissionState.launchPermissionRequest()
            }
        },
        onAddLocation = {
            if (locationPermissionState.status == PermissionStatus.Granted) {
                viewModel.navigateToLocationSelect()
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        },
        onLocationAttachmentRemove = {
            viewModel.onLocationRemove()
            sharedViewModel.updatePostCreateSharedData(null, null)
        },
        haveAccessToFile = filePermissionState.status == PermissionStatus.Granted,
        haveAccessToLocation = locationPermissionState.status == PermissionStatus.Granted,
        onCancel = { viewModel.onCancel() },
        onConfirm = { viewModel.post() },
        onPostTextUpdate = { viewModel.onTextFieldUpdated(it) }
    )

}

@Composable
private fun PostUI(
    uiState: PostItemUiState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onAddImage: () -> Unit,
    onAddLocation: () -> Unit,
    haveAccessToFile: Boolean,
    haveAccessToLocation: Boolean,
    onPostTextUpdate: (String) -> Unit,
    onImageAttachmentRemove: (String) -> Unit,
    onLocationAttachmentRemove: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopRow(
                onCancel = onCancel,
                onConfirm = onConfirm,
                confirmText = stringResource(R.string.send)
            )
        },
        bottomBar = {
            ToolBar(
                onAddImage = onAddImage,
                onAddLocation = onAddLocation,
                haveAccessToFile = haveAccessToFile,
                haveAccessToLocation = haveAccessToLocation
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ContentEditor(
                uiState = uiState,
                onTextUpdate = onPostTextUpdate,
                modifier = Modifier.weight(1f)
            )
            Attachments(
                attachedImages = uiState.attachedImages,
                locationAttached = uiState.latitude != null,
                onImageAttachmentRemove = onImageAttachmentRemove,
                onLocationRemove = onLocationAttachmentRemove,
            )
        }
    }
}

@Composable
private fun ContentEditor(
    uiState: PostItemUiState,
    onTextUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = uiState.content,
        onValueChange = onTextUpdate,
        modifier = modifier.fillMaxWidth(),
        singleLine = false,
    )
}

@Composable
fun Attachments(
    attachedImages: List<String>,
    locationAttached: Boolean,
    onImageAttachmentRemove: (String) -> Unit,
    onLocationRemove: () -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(paddingMedium),
        contentPadding = PaddingValues(paddingMedium)
    ) {
        items(attachedImages) {
            ImageAttachment(imageUrl = it, onCloseButtonClick = { onImageAttachmentRemove(it) })
        }
        if (locationAttached) {
            item {
                LocationAttachment(onCloseButtonClick = onLocationRemove)
            }
        }
    }
}

@Composable
private fun TopRow(
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Close, contentDescription = "cancel")
        }
        Button(
            onClick = onConfirm,
            shape = RoundedCornerShape(50)
        ) {
            Text(confirmText)
        }
    }
}

@Composable
private fun ToolBar(
    haveAccessToFile: Boolean,
    haveAccessToLocation: Boolean,
    onAddImage: () -> Unit,
    onAddLocation: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onAddImage
        ) {
            Icon(
                Icons.Default.Add,
                tint = MaterialTheme.colors.onSurface.copy(alpha = if (haveAccessToFile) 1f else 0.4f),
                contentDescription = "Add images",
            )
        }
        IconButton(
            onClick = onAddLocation
        ) {
            Icon(
                painterResource(R.drawable.ic_pin_drop),
                tint = MaterialTheme.colors.onSurface.copy(alpha = if (haveAccessToLocation) 1f else 0.4f),
                contentDescription = "Add location",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopRowPreview() {
    FooSTheme {
        TopRow(confirmText = "Confirm", onCancel = { }, onConfirm = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolBarPreview() {
    FooSTheme {
        ToolBar(
            haveAccessToFile = true,
            haveAccessToLocation = true,
            onAddImage = { },
            onAddLocation = { }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PostUIPreview() {
    FooSTheme {
        PostUI(
            uiState = PostItemUiState.Default,
            onCancel = { },
            onConfirm = { },
            onAddImage = { },
            onAddLocation = { },
            haveAccessToFile = true,
            haveAccessToLocation = false,
            onPostTextUpdate = { },
            onImageAttachmentRemove = { },
            onLocationAttachmentRemove = { }
        )
    }
}

