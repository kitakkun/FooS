package com.example.foos.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.foos.R
import com.example.foos.ui.component.AsyncUserIcon

/**
 * タイムラインに表示する投稿アイテム一つのコンポーザブル
 */
@Preview(showBackground = true)
@Composable
fun PostItem(
    postItemUiState: PostItemUiState = PostItemUiState.Default,
    onContentClick: (String) -> Unit = {},
    onPostItemClick: (List<String>) -> Unit = { },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        PostContentRow(postItemUiState, onContentClick, onPostItemClick)
        ReactionRow(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun PostContentRow(
    postItemUiState: PostItemUiState = PostItemUiState.Default,
    onContentClick: (String) -> Unit = {},
    onPostItemClick: (List<String>) -> Unit = {}
) {
    Row {
        AsyncUserIcon(url = postItemUiState.userIcon)
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.clickable {
                onContentClick.invoke(postItemUiState.postId)
            }
        ) {
            UserIdentifyRow(postItemUiState.username, postItemUiState.userId)
            Text(
                text = postItemUiState.content,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
            )
            Spacer(Modifier.height(16.dp))
            AttachedImagesRow(postItemUiState.attachedImages, onPostItemClick)
        }
    }
}

@Composable
fun UserIdentifyRow(
    username: String,
    userId: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text(text = "@${userId}", maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun AttachedImagesRow(
    images: List<String>,
    onPostItemClick: (List<String>) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(images) { image ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_no_image)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onPostItemClick.invoke(images) }
                    .size(120.dp)
                    .clip(RoundedCornerShape(10)),

                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun ZoomableImage(
    url: String
) {
    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(1f) }
    Box(
        modifier = Modifier
            .clip(RectangleShape) // Clip the box content
            .fillMaxSize() // Give the size you want...
            .background(Color.Gray)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
                    rotationState.value += rotation
                }
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url).crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            modifier = Modifier
                .align(Alignment.Center) // keep the image centralized into the Box
                .graphicsLayer(
                    // adding some zoom limits (min 50%, max 200%)
                    scaleX = maxOf(.5f, minOf(3f, scale.value)),
                    scaleY = maxOf(.5f, minOf(3f, scale.value)),
                    rotationZ = rotationState.value
                ),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun UserIdentifyRowPreview() {
    UserIdentifyRow(username = "username", userId = "userid")
}
