package com.github.kitakkun.foos.post.create

import android.Manifest
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
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.FooSTheme
import com.github.kitakkun.foos.post.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

/**
 * 投稿画面のコンポーザブル
 * テキスト、画像、位置情報を指定することが可能。
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostEditScreen(
    viewModel: PostCreateViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
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

    PostEditUI(
        uiState = uiState,
        onImageAttachmentRemove = { viewModel.removeImageAttachment(it) },
        onAddImage = {
            if (filePermissionState.status == PermissionStatus.Granted) {
                launcher.launch("image/*")
            } else {
                filePermissionState.launchPermissionRequest()
            }
        },
        onAddLocation = {
            if (locationPermissionState.status == PermissionStatus.Granted) {
                navController.navigate(PostScreenRouter.PostCreate.LocationSelect.route)
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        },
        onLocationAttachmentRemove = {
            viewModel.removeLocationData()
        },
        haveAccessToFile = filePermissionState.status == PermissionStatus.Granted,
        haveAccessToLocation = locationPermissionState.status == PermissionStatus.Granted,
        onCancel = {
            navController.navigateUp()
        },
        onConfirm = { viewModel.post(context) },
        onPostTextUpdate = { viewModel.updateContentText(it) }
    )

}

@Composable
private fun PostEditUI(
    uiState: PostEditUiState,
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
            PostEditTopAppBar(
                onCancel = onCancel,
                onConfirm = onConfirm,
                confirmText = stringResource(R.string.send)
            )
        },
        bottomBar = {
            PostEditBottomToolBar(
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
                attachedImages = uiState.attachedImageUrls,
                locationAttached = uiState.location?.latitude != null,
                onImageAttachmentRemove = onImageAttachmentRemove,
                onLocationRemove = onLocationAttachmentRemove,
            )
        }
    }
}

@Composable
private fun ContentEditor(
    uiState: PostEditUiState,
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
private fun PostEditTopAppBar(
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
private fun PostEditBottomToolBar(
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
                imageVector = Icons.Default.PinDrop,
                tint = MaterialTheme.colors.onSurface.copy(alpha = if (haveAccessToLocation) 1f else 0.4f),
                contentDescription = "Add location",
            )
        }
    }
}

@Preview
@Composable
private fun TopRowPreview() = PreviewContainer {
    PostEditTopAppBar(confirmText = "Confirm", onCancel = { }, onConfirm = { })
}

@Preview
@Composable
private fun ToolBarPreview() = PreviewContainer {
    PostEditBottomToolBar(
        haveAccessToFile = true,
        haveAccessToLocation = true,
        onAddImage = { },
        onAddLocation = { }
    )
}

@Preview
@Composable
private fun PostUIPreview() = PreviewContainer {
    FooSTheme {
        PostEditUI(
            uiState = PostEditUiState(),
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
