package com.example.foos.ui.composable

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.ui.IconGenerator


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapContent(navController: NavController) {

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    when (locationPermissionState.status) {
        PermissionStatus.Granted -> { Map() }
        else -> { MapDeniedView(locationPermissionState) }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun Map() {
    val properties by remember {
        mutableStateOf(
            MapProperties(isMyLocationEnabled = true)
        )
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(myLocationButtonEnabled = true))
    }

    var latLng by remember { mutableStateOf(LatLng(1.35, 103.87)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 20f)
    }

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        LocalContext.current
    )

    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            Log.d("LOCATION", location.toString())
            latLng = LatLng(it.latitude, it.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 20f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
    ) {
        val iconGenerator = IconGenerator(LocalContext.current)
        iconGenerator.setBackground(null)
        val textView = TextView(LocalContext.current)
        textView.text = "Hello"
        iconGenerator.setContentView(textView)

        Marker(
            state = MarkerState(position = latLng),
            icon = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())
        )


    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapDeniedView(
    locationPermissionState: PermissionState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please give the permission."
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }
}