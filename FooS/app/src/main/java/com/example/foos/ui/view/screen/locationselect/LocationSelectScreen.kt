package com.example.foos.ui.view.screen.locationselect

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
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.view.screen.ScreenViewModel
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
    viewModel: LocationSelectViewModel,
    navController: NavController,
    sharedViewModel: ScreenViewModel,
) {

    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.navToNextEvent.collect {
            navController.navigate(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cancelNavEvent.collect {
            navController.navigateUp()
        }
    }

    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        LocalContext.current
    )

    val permissionStatus =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        if (permissionStatus.status == PermissionStatus.Granted) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.select_pin_message)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.cancel() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.navigateToConfirmScreen()
                    }) {
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
            onMapClick = {
                sharedViewModel.updatePostCreateSharedData(
                    location = it,
                    locationName = null
                )
                viewModel.updatePinPosition(it)
            },
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {
            uiState.pinPosition?.let {
                Marker(
                    state = MarkerState(it)
                )
            }
        }
    }
}