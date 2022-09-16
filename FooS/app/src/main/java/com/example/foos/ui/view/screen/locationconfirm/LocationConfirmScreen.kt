package com.example.foos.ui.view.screen.locationconfirm

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.screen.ScreenViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun LocationConfirmScreen(
    viewModel: LocationConfirmViewModel,
    navController: NavController,
    sharedViewModel: ScreenViewModel,
) {

    var locationName by remember {
        mutableStateOf("")
    }
    val location = sharedViewModel.postCreateSharedData.value.location ?: return
    val context = LocalContext.current

    // キャンセル時のイベント（前の画面に戻る）
    LaunchedEffect(Unit) {
        launch {
            viewModel.cancelEvent.collect {
                navController.navigateUp()
            }
        }
        launch {
            // 完了時のイベント（投稿画面へ戻る）
            viewModel.completeEvent.collect {
                if (locationName.isEmpty()) {
                    Toast.makeText(
                        context, context.getString(R.string.specify_location_alert_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@collect
                }
                sharedViewModel.updatePostCreateSharedData(location, locationName)
                navController.navigate(SubScreen.PostCreate.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }
    }

    LocationConfirmUI(
        location = location,
        locationName = locationName,
        onCancel = { viewModel.backToPrevScreen() },
        onProceed = { viewModel.completeStep() },
        onLocationNameUpdated = {
            locationName = it
            sharedViewModel.updatePostCreateSharedData(location, it)
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LocationConfirmUIPreview() {
    FooSTheme {
        LocationConfirmUI(
            locationName = "LocationName",
            location = LatLng(122.0, 154.0),
            onCancel = {},
            onProceed = {},
            onLocationNameUpdated = {}
        )
    }
}
