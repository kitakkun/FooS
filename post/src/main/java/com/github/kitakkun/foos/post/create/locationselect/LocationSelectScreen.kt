package com.github.kitakkun.foos.post.create.locationselect

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.post.R
import com.github.kitakkun.foos.post.create.PostCreateViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSelectScreen(
    viewModel: PostCreateViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        LocalContext.current
    )

    val permissionStatus =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        if (permissionStatus.status == PermissionStatus.Granted) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
                location.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }

    LocationSelectUI(
        cameraPositionState = cameraPositionState,
        pinLocation = uiState.location,
        onCancel = {
            viewModel.removeLocationData()
            navController.navigateUp()
        },
        onConfirm = {
            navController.navigate(PostScreenRouter.PostCreate.LocationConfirm.route)
        },
        onMapClick = {
            viewModel.setLocation(it)
        }
    )
}

@Composable
private fun LocationSelectUI(
    cameraPositionState: CameraPositionState,
    pinLocation: LatLng?,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onMapClick: (LatLng) -> Unit
) {

    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.select_pin_message)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onConfirm, enabled = pinLocation != null) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = onMapClick,
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {
            pinLocation?.let {
                Marker(
                    state = MarkerState(it)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LocationSelectUIPreview() = PreviewContainer {
    val cameraPositionState = rememberCameraPositionState()
    LocationSelectUI(
        cameraPositionState = cameraPositionState,
        pinLocation = null,
        onCancel = { },
        onConfirm = { },
        onMapClick = { },
    )
}
