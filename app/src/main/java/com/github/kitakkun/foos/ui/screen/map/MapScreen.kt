package com.github.kitakkun.foos.ui.screen.map

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.kitakkun.foos.R
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.common.const.paddingSmall
import com.github.kitakkun.foos.customview.composable.post.PostDetailView
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
    val coroutineScope = rememberCoroutineScope()

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

    MapUI(
        uiState = uiState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        hasLocationPermission = locationPermissionState.status == PermissionStatus.Granted,
        cameraPositionState = cameraPositionState,
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
        onLocationRequestButtonClick = { locationPermissionState.launchPermissionRequest() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MapUI(
    uiState: MapScreenUiState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    hasLocationPermission: Boolean,
    cameraPositionState: CameraPositionState,
    onLoad: (LatLngBounds) -> Unit,
    onBubbleClick: (PostItemUiState) -> Unit,
    onLocationRequestButtonClick: () -> Unit,
) {
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
                PostDetailView(
                    uiState = uiState.focusingPost,
                    onReactionRemoved = { /* TODO */ },
                    onGoogleMapsClicked = { /* TODO */ },
                    onReactionButtonClicked = { /* TODO */ },
                    onUserInfoClicked = { /* TODO */ }
                )
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        if (hasLocationPermission) {
            Map(
                cameraPositionState = cameraPositionState,
                posts = uiState.posts,
                onLoad = onLoad,
                onBubbleClick = onBubbleClick,
                onMapClick = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            )
        } else {
            MapDeniedView(onLocationRequestButtonClick = onLocationRequestButtonClick)
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
                    state = MarkerState(
                        position = LatLng(post.latitude as Double, post.longitude as Double)
                    ),
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

@Composable
private fun MapDeniedView(
    onLocationRequestButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.permission_request_message))
        Spacer(Modifier.height(paddingMedium))
        Button(onClick = onLocationRequestButtonClick) {
            Text(text = stringResource(id = R.string.request_permission))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun MapUIPreview() = PreviewContainer {
    MapUI(
        uiState = MapScreenUiState.Default,
        bottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
        hasLocationPermission = false,
        cameraPositionState = rememberCameraPositionState(),
        onLoad = {},
        onBubbleClick = {},
        onLocationRequestButtonClick = {}
    )
}
