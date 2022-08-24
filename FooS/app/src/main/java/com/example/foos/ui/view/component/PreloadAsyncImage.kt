package com.example.foos.ui.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.foos.R

@Composable
fun PreloadAsyncImage(
    url: String,
    model: ImageRequest,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        context.imageLoader.enqueue(request)
    }
    AsyncImage(
        model = model,
        contentScale = contentScale,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
