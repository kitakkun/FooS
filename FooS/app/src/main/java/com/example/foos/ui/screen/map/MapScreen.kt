package com.example.foos.ui.screen.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/**
 * グルメマップ画面のコンポーザブル
 * マップ上に口コミを吹き出しで表示します
 *
 *  ManifestにPermissionを定義しても警告が出続けるため、MissingPermissionを無視
 *  参考 -> https://stackoverflow.com/questions/45279370/android-studio-complains-about-missing-permission-check-for-fingerprint-sensor
 */
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel, navController: NavController) {

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    var latLng by remember { mutableStateOf(LatLng(1.35, 103.87)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 20f)
    }


    when (locationPermissionState.status) {
        PermissionStatus.Granted -> {
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
            Map(cameraPositionState)
        }
        else -> {
            MapDeniedView(locationPermissionState)
        }
    }
}

@Composable
fun Map(
    cameraPositionState: CameraPositionState
) {
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true) ) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
    ) {
//        val iconGenerator = IconGenerator(LocalContext.current)
//        iconGenerator.setBackground(null)
//        val textView = TextView(LocalContext.current)
//        textView.text = "Hello"
//        iconGenerator.setContentView(textView)
//
//        Marker(
//            state = MarkerState(position = latLng),
//            icon = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())
//        )

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