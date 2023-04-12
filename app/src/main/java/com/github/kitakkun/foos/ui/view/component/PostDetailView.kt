package com.github.kitakkun.foos.ui.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.github.kitakkun.foos.ui.PreviewContainer
import com.github.kitakkun.foos.ui.constants.paddingLarge
import com.github.kitakkun.foos.ui.constants.paddingMedium
import com.github.kitakkun.foos.ui.state.component.PostItemUiState
import com.github.kitakkun.foos.ui.theme.FooSTheme
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
    onUserInfoClicked: (String) -> Unit,
    onReactionButtonClicked: (String) -> Unit,
    onReactionRemoved: () -> Unit,
    onGoogleMapsClicked: () -> Unit,
) {
    /* TODO: このFirebaseのuidを用いたロジックはビューの外へ抽出したい */
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

@Preview
@Composable
private fun PostDetailPreview() = PreviewContainer {
    FooSTheme {
        val uiState = PostItemUiState.Default.copy(
            username = "username",
            userId = "userId",
            content = "content..."
        )
        PostDetailView(
            uiState = uiState,
            onReactionRemoved = {},
            onGoogleMapsClicked = {},
            onReactionButtonClicked = {},
            onUserInfoClicked = {}
        )
    }
}
