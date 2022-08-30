package com.example.foos.ui.view.screen.locationconfirm

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.foos.R
import com.example.foos.ui.view.screen.Page
import com.example.foos.ui.view.screen.ScreenViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LocationConfirmScreen(
    viewModel: LocationConfirmViewModel,
    navController: NavController,
    sharedViewModel: ScreenViewModel,
) {

    val uiState = viewModel.uiState.value
    val location = sharedViewModel.postCreateSharedData.value.location ?: return
    val context = LocalContext.current

    // キャンセル時のイベント（前の画面に戻る）
    LaunchedEffect(Unit) {
        viewModel.cancelEvent.collect {
            navController.navigateUp()
        }
    }

    // 完了時のイベント（投稿画面へ戻る）
    LaunchedEffect(Unit) {
        viewModel.completeEvent.collect {
            val value = viewModel.uiState.value.locationName
            if (value.isEmpty() || value.isBlank()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.specify_location_alert_message),
                    Toast.LENGTH_SHORT
                ).show()
                return@collect
            }
            sharedViewModel.updatePostCreateSharedData(location, uiState.locationName)
            navController.navigate(Page.PostCreate.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.confirm_location)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.backToPrevScreen() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.completeStep() }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val focusManager = LocalFocusManager.current
            Text(text = stringResource(R.string.enter_location_name_message))
            TextField(
                value = uiState.locationName,
                onValueChange = { viewModel.updateLocationName(it) },
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