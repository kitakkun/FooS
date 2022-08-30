package com.example.foos.ui.view.screen.postdetail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.view.component.UserIcon
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@Composable
fun PostDetailScreen(viewModel: PostDetailViewModel, navController: NavController, postId: String) {

    LaunchedEffect(Unit) {
        viewModel.fetchPost(postId)
    }

    val uiState = viewModel.uiState.value
    val postItemUiState = uiState.postItemUiState

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(url = postItemUiState.userIcon, onClick = { viewModel.onUserInfoClicked() })
            Spacer(Modifier.width(24.dp))
            UserIdentityColumn(
                username = postItemUiState.username,
                userId = postItemUiState.userId,
                onClick = { viewModel.onUserInfoClicked() })
            Spacer(Modifier.weight(1f))
            ReactionButton(onReactionClicked = { reaction ->
                viewModel.onReactionButtonClicked(
                    reaction
                )
            })
        }
        Spacer(Modifier.height(24.dp))
        Text(postItemUiState.content)
        Spacer(Modifier.height(24.dp))
        AttachedImagesDisplay(urls = postItemUiState.attachedImages)
        Spacer(Modifier.height(24.dp))
        LocationMap(
            postItemUiState.latitude,
            postItemUiState.longitude,
            onClick = { viewModel.onGoogleMapsClicked() })
    }
}

@Composable
fun UserIdentityColumn(
    username: String,
    userId: String,
    onClick: () -> Unit = {},
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
    onClick: () -> Unit = {},
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
                onClick.invoke()
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
        horizontalArrangement = Arrangement.spacedBy(paddingMedium)
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

/**
 * リアクション追加用ボタン
 * @param onReactionClicked リアクション追加時の挙動
 */
@Composable
fun ReactionButton(
    onReactionClicked: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(painterResource(R.drawable.ic_add_reaction), null)
        ReactionDropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            onReactionClicked = onReactionClicked
        )
    }
}

/**
 * Reactionを行うためのドロップダウン
 * @param expanded ドロップダウンを開くかどうか
 * @param onDismissRequest ドロップダウンメニューの範囲外をタップしたときの挙動
 * @param onReactionClicked リアクションが行われた際の挙動
 */
@Composable
fun ReactionDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onReactionClicked: (String) -> Unit,
) {
    val reactions = listOf(
        stringResource(id = R.string.emoji_like),
        stringResource(id = R.string.emoji_yummy),
        stringResource(id = R.string.emoji_fire),
    )

    val dropdownWidth = with(LocalDensity.current) {
        60.sp.toDp()
    }
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(45))) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(dropdownWidth)
        ) {
            reactions.forEach {
                DropdownMenuItem(onClick = { onReactionClicked.invoke(it) }) {
                    Text(
                        text = it,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}