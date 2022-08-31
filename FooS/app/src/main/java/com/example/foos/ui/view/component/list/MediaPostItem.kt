package com.example.foos.ui.view.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.ui.state.screen.home.PostItemUiState

@Composable
fun MediaPostItem(
    uiState: PostItemUiState,
    modifier: Modifier = Modifier,
    onContentClick: (String) -> Unit = {},
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uiState.attachedImages[0]).crossfade(true).build(),
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
               onContentClick(uiState.postId)
            },
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
private fun MediaPostItemPreview() {
    MediaPostItem(uiState = PostItemUiState.Default)
}