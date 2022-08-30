package com.example.foos.ui.view.screen.post

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.state.screen.post.PostScreenUiState
import com.example.foos.ui.view.screen.ScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

/**
 * 投稿画面のコンポーザブル
 * テキスト、画像、位置情報を指定することが可能。
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel,
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
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navUpEvent.collect {
            navController.navigateUp()
        }
    }

    val uiState = viewModel.uiState.value

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

    SideEffect {
        if (filePermissionState.status != PermissionStatus.Granted) {
            filePermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopRow(
                onCanceled = { navController.navigateUp() },
                onConfirmed = {
                    viewModel.post()
                },
                confirmText = "Send",
            )
        },
        bottomBar = {
            ToolBar(
                onAddImagesBtnClicked = {
                    launcher.launch("image/*")
                },
                onLocationAddButtonClicked = { viewModel.navigateToLocationSelect() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ContentEditor(
                uiState = uiState,
                onTextUpdate = { viewModel.onTextFieldUpdated(it) },
            )
            Attachments(
                attachedImages = uiState.attachedImages,
                locationAttached = locationAttached
            )
        }
    }
}

@Composable
private fun ColumnScope.ContentEditor(
    uiState: PostScreenUiState,
    onTextUpdate: (String) -> Unit = {},
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
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        singleLine = false,
    )
}

@Composable
fun Attachments(
    attachedImages: List<String>,
    locationAttached: Boolean,
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
                        .size(100.dp)
                        .clip(RoundedCornerShape(10))
                )
                Icon(Icons.Default.Close, contentDescription = "remove Image")
            }
        }
        if (locationAttached) {
            item {
                Column(
                    modifier = Modifier
                        .border(width = 1.dp, color = MaterialTheme.colors.onSurface)
                        .size(100.dp)
                        .clip(RoundedCornerShape(10)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "remove location data",
                        modifier = Modifier.align(Alignment.End)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_pin_drop),
                        tint = MaterialTheme.colors.onSurface,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.location_attached),
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TopRow(
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
private fun ToolBar(
    onAddImagesBtnClicked: () -> Unit = {},
    onLocationAddButtonClicked: () -> Unit = {},
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
            onClick = onLocationAddButtonClicked
        ) {
            Icon(
                painterResource(R.drawable.ic_pin_drop),
                contentDescription = "Add location",
            )
        }
    }
}