package com.example.foos.ui.view.screen.postdetail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

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
            Spacer(Modifier.weight(1f))
            ReactionButton()
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
        Text(text = userId, fontWeight = FontWeight.Light, fontSize = 12.sp)
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
                .height(300.dp),
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

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun AttachedImagesDisplay(
    urls: List<String>,
) {
    val lazyListState = rememberLazyListState()
    LazyRow(
        state = lazyListState,
        flingBehavior = rememberSnapperFlingBehavior(lazyListState),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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

@Composable
fun ReactionButton(

) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(painterResource(R.drawable.ic_add_reaction), null)
        ReactionDropdown(expanded = expanded, onDismissRequest = { expanded = false })
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReactionDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val dropdownWidth = with(LocalDensity.current) {
        60.sp.toDp()
    }
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(45))) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(dropdownWidth)
        ) {
            DropdownMenuItem(onClick = {}) {
                Text(
                    text = stringResource(id = R.string.emoji_like),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
            DropdownMenuItem(onClick = {}) {
                Text(
                    text = stringResource(id = R.string.emoji_yummy),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}