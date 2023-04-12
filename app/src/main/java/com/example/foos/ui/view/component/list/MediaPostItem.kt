package com.example.foos.ui.view.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.foos.ui.PreviewContainer
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.view.component.MaxSizeLoadingIndicator

@Composable
fun MediaPostItem(
    uiState: PostItemUiState,
    modifier: Modifier = Modifier,
    onContentClick: (String) -> Unit = {},
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uiState.attachedImages[0]).crossfade(true).build(),
        contentDescription = null,
        loading = {
            MaxSizeLoadingIndicator()
        },
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                onContentClick(uiState.postId)
            },
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
private fun MediaPostItemPreview() = PreviewContainer {
    MediaPostItem(uiState = PostItemUiState.Default)
}
