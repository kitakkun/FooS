package com.example.foos.ui.view.screen.postdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.view.component.UserIcon
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    PostDetailScreen(viewModel = hiltViewModel(), navController = rememberNavController())
}

@Composable
fun PostDetailScreen(viewModel: PostDetailViewModel, navController: NavController) {

    val uiState = viewModel.uiState.collectAsState()
    val postItemUiState = uiState.value.postItemUiState

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(url = postItemUiState.userIcon)
            Spacer(Modifier.width(24.dp))
            UserIdentityColumn(username = postItemUiState.username, userId = postItemUiState.userId)
        }
        Spacer(Modifier.height(24.dp))
        Text(postItemUiState.content)
        Spacer(Modifier.height(24.dp))
        AttachedImagesDisplay(urls = postItemUiState.attachedImages)
        Spacer(Modifier.height(24.dp))
        LocationMap(postItemUiState.latitude, postItemUiState.longitude)
    }
}

@Composable
fun UserIdentityColumn(
    username: String,
    userId: String,
) {
    Column() {
        Text(text = username, fontWeight = FontWeight.Bold)
        Text(text = userId, fontWeight = FontWeight.Light)
    }
}

@Composable
fun LocationMap(
    latitude: Double?,
    longitude: Double?,
) {
    if (longitude != null && latitude != null) {
        val properties by remember { mutableStateOf(MapProperties()) }
        val uiSettings by remember { mutableStateOf(MapUiSettings()) }

        val latLng = LatLng(latitude, longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, 15f)
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10))
                .height(200.dp),
            cameraPositionState = cameraPositionState,
            onMapClick = {

            },
            properties = properties,
            uiSettings = uiSettings,
        ) {
            Marker(
                state = MarkerState(position = latLng),
            )
        }
    }
}

@Composable
fun AttachedImagesDisplay(
    urls: List<String>,
) {
    LazyRow(
    ) {
        items(urls) { image ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_no_image)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10)),

                contentScale = ContentScale.Crop,
            )
        }
    }

}