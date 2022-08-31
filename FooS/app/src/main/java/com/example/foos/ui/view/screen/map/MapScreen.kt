package com.example.foos.ui.view.screen.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.constants.paddingSmall
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.view.component.PostDetailView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.google.maps.android.ui.IconGenerator
import kotlinx.coroutines.launch

/**
 * グルメマップ画面のコンポーザブル
 * マップ上に口コミを吹き出しで表示します
 *
 *  ManifestにPermissionを定義しても警告が出続けるため、MissingPermissionを無視
 *  参考 -> https://stackoverflow.com/questions/45279370/android-studio-complains-about-missing-permission-check-for-fingerprint-sensor
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(viewModel: MapViewModel, navController: NavController) {

    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            navController.navigate(it)
        }
    }

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    var latLng by remember { mutableStateOf(LatLng(1.35, 103.87)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (locationPermissionState.status == PermissionStatus.Granted) {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                context
            )
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState((BottomSheetValue.Collapsed))
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .padding(paddingSmall)
                        .width(60.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colors.onSurface)
                )
                PostDetailView(uiState = uiState.focusingPost)
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        when (locationPermissionState.status) {
            PermissionStatus.Granted -> Map(
                cameraPositionState = cameraPositionState,
                posts = uiState.posts,
                onLoad = { viewModel.fetchNearbyPosts(it) },
                onBubbleClick = {
                    coroutineScope.launch {
                        viewModel.showPostDetail(it)
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                },
                onMapClick = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            )
            else -> MapDeniedView(locationPermissionState)
        }
    }
}

@Composable
fun Map(
    cameraPositionState: CameraPositionState,
    posts: List<PostItemUiState>,
    onLoad: (LatLngBounds) -> Unit,
    onBubbleClick: (PostItemUiState) -> Unit,
    onMapClick: (LatLng) -> Unit,
) {
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapLoaded = {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let(onLoad)
        },
        onMapClick = onMapClick,
    ) {
        posts.forEach { post ->
            val iconGenerator = IconGenerator(LocalContext.current)
            iconGenerator.setBackground(null)
            val imageView = ImageView(LocalContext.current)
            imageView.setImageResource(R.drawable.ic_bubble)
            imageView.layoutParams = ViewGroup.LayoutParams(170, 170)
            imageView.setOnClickListener {
            }
            iconGenerator.setContentView(imageView)
            if (post.latitude != null && post.longitude != null) {
                Marker(
                    state = MarkerState(position = LatLng(post.latitude, post.longitude)),
                    icon = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()),
                    onClick = {
                        onBubbleClick(post)
                        false
                    }
                )
            }

        }

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
        Spacer(Modifier.height(paddingMedium))
        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }
}