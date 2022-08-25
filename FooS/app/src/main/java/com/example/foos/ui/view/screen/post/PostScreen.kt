package com.example.foos.ui.view.screen.post

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.state.screen.post.PostScreenUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

/**
 * 投稿画面のコンポーザブル
 * テキスト、画像、位置情報を指定することが可能。
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostScreen(viewModel: PostViewModel, navController: NavController) {
    val uiState by viewModel.postUiState.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    )
    {
        viewModel.setImages(context, it)
    }

    val filePermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )

    PostUI(
        onCanceled = { navController.navigateUp() },
        onSent = {
            viewModel.post(navController)
        },
        onAddImagesBtnClicked = {
            if (filePermissionState.status == PermissionStatus.Granted) {
                launcher.launch("image/*")
            } else {
                filePermissionState.launchPermissionRequest()
                launcher.launch("image/*")
            }
        },
        uiState = uiState,
        onTextUpdate = { viewModel.onTextFieldUpdated(it) },
    )
}

@Composable
fun PostUI(
    uiState: PostScreenUiState,
    onCanceled: () -> Unit = {},
    onSent: () -> Unit = {},
    onAddImagesBtnClicked: () -> Unit = {},
    onTextUpdate: (String) -> Unit = {},
) {
    Column {
        PostUITopRow(
            confirmText = "Send",
            onCanceled = onCanceled,
            onConfirmed = onSent
        )
        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = uiState.content,
            onValueChange = onTextUpdate,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = false,
        )
        AttachedImagesRow(attachedImages = uiState.attachedImages)
        ToolBar(
            onAddImagesBtnClicked = onAddImagesBtnClicked
        )
    }
}

@Composable
fun AttachedImagesRow(
    attachedImages: List<String>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(attachedImages) {
            Box(contentAlignment = Alignment.TopEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it).build(), contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(25))
                )
                Icon(Icons.Default.Close, contentDescription = "remove Image")
            }
        }
    }
}

@Preview
@Composable
fun PostUITopRow(
    confirmText: String = "Confirm",
    onCanceled: () -> Unit = {},
    onConfirmed: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onCanceled) {
            Icon(Icons.Default.Close, contentDescription = "cancel")
        }
        Button(
            onClick = onConfirmed,
            shape = RoundedCornerShape(50)
        ) {
            Text(confirmText)
        }
    }
}

@Preview
@Composable
fun ToolBar(
    onAddImagesBtnClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onAddImagesBtnClicked
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add images",
            )
        }
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                painterResource(R.drawable.ic_pin_drop),
                contentDescription = "Add location",
            )
        }
    }
}