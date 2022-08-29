package com.example.foos.ui.view.screen.locationconfirm

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationConfirmScreen(
    viewModel: LocationConfirmViewModel,
    navController: NavController,
    location: LatLng
) {

    val location = navController.previousBackStackEntry?.savedStateHandle?.get<LatLng>("location")

    location?.let {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 15f)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(R.string.enter_location_name_message))
            TextField(value = "test", onValueChange = {})
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                Marker(
                    state = MarkerState(position = location)
                )
            }
        }

    }
}