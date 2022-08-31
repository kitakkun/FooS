package com.example.foos.ui.view.component

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.constants.paddingLarge
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.state.component.PostItemUiState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@Composable
fun PostDetailView(
    uiState: PostItemUiState,
    onUserInfoClicked: (String) -> Unit = {},
    onReactionButtonClicked: (String) -> Unit = {},
    onReactionRemoved: () -> Unit = {},
    onGoogleMapsClicked: () -> Unit = {},
) {
    var myReaction = uiState.reactions.find { it.from == Firebase.auth.uid }?.reaction
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingLarge)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserIcon(url = uiState.userIcon, onClick = { onUserInfoClicked(uiState.userId) })
            Spacer(Modifier.width(paddingLarge))
            VerticalUserIdentityText(
                username = uiState.username,
                userId = uiState.userId,
                modifier = Modifier.clickable { onUserInfoClicked(uiState.userId) }
            )
            Spacer(Modifier.weight(1f))
            ReactionButton(
                onReactionClicked = {
                    onReactionButtonClicked(it)
                    myReaction = it
                },
                onReactionRemoved = {
                    onReactionRemoved()
                    myReaction = null
                },
                myReaction = myReaction,
            )
        }
        Spacer(Modifier.height(paddingLarge))
        Text(uiState.content)
        Spacer(Modifier.height(paddingLarge))
        AttachedImagesDisplay(urls = uiState.attachedImages)
        Spacer(Modifier.height(paddingLarge))
        LocationMap(
            uiState.latitude,
            uiState.longitude,
            onClick = { onGoogleMapsClicked() })
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
    onReactionRemoved: () -> Unit,
    myReaction: String?,
) {
    var expanded by remember { mutableStateOf(false) }
    if (myReaction == null) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(painterResource(R.drawable.ic_add_reaction), null)
            ReactionDropdown(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onReactionClicked = { onReactionClicked(it) }
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colors.surface, shape = CircleShape)
                .background(MaterialTheme.colors.surface)
                .clickable { onReactionRemoved() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = myReaction,
                fontSize = 25.sp,
            )
        }
    }
}

/**
 * Reactionを行うためのドロップダウン
 * @param expanded ドロップダウンを開くかどうか
 * @param onDismissRequest ドロップダウンメニューの範囲外をタップしたときの挙動
 * @param onReactionClicked リアクションが行われた際の挙動
 */
@OptIn(ExperimentalAnimationApi::class)
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

    val density = LocalDensity.current

    val dropdownWidth = with(LocalDensity.current) {
        60.sp.toDp()
    }

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.CenterVertically),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
    ) {
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

}
