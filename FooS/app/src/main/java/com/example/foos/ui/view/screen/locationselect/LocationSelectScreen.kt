package com.example.foos.ui.view.screen.locationselect

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.foos.R
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun LocationSelectScreen(viewModel: LocationSelectViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navUpEvent.collect {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "location",
                uiState.value.pinPosition
            )
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.select_pin_message)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        GoogleMap(
            onMapClick = { viewModel.updatePinPosition(it) },
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {
            Marker(
                state = MarkerState(uiState.value.pinPosition)
            )
        }
    }
}