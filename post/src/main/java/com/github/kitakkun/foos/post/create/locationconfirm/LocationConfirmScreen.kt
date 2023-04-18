package com.github.kitakkun.foos.post.create.locationconfirm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.post.R
import com.github.kitakkun.foos.post.create.PostCreateViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun LocationConfirmScreen(
    viewModel: PostCreateViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.location == null) {
            navController.navigateUp()
        }
    }

    LocationConfirmUI(
        location = uiState.location ?: LatLng(0.0, 0.0),
        locationName = uiState.locationName ?: "",
        onCancel = {
            navController.navigateUp()
        },
        onProceed = {
            navController.navigate(PostScreenRouter.PostCreate.route) {
                popUpTo(PostScreenRouter.PostCreate.route) { inclusive = true }
            }
        },
        onLocationNameUpdated = {
            viewModel.updateLocationName(it)
        }
    )
}

@Composable
private fun LocationConfirmUI(
    locationName: String,
    location: LatLng,
    onCancel: () -> Unit,
    onProceed: () -> Unit,
    onLocationNameUpdated: (String) -> Unit,
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.confirm_location)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onProceed) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(paddingMedium),
            modifier = Modifier
                .padding(innerPadding)
                .padding(paddingMedium)
        ) {
            val focusManager = LocalFocusManager.current
            val mapUiSettings by remember {
                mutableStateOf(
                    MapUiSettings(
                        mapToolbarEnabled = false,
                        myLocationButtonEnabled = false,
                        scrollGesturesEnabledDuringRotateOrZoom = false,
                        zoomGesturesEnabled = false,
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        rotationGesturesEnabled = false,
                    )
                )
            }
            Text(text = stringResource(R.string.enter_location_name_message))
            TextField(
                value = locationName,
                onValueChange = onLocationNameUpdated,
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            GoogleMap(
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Marker(
                    state = MarkerState(position = location)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LocationConfirmUIPreview() = PreviewContainer {
    LocationConfirmUI(
        locationName = "LocationName",
        location = LatLng(122.0, 154.0),
        onCancel = {},
        onProceed = {},
        onLocationNameUpdated = {}
    )
}
