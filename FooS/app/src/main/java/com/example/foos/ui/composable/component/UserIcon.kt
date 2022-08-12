package com.example.foos.ui.composable.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.foos.R

@Composable
fun UserIcon(
    icon: Int = R.drawable.ic_launcher_foreground,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = modifier
            .width(50.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .border(1.dp, Color.Gray)
    )
}

@Composable
fun AsyncUserIcon(
    url: String,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url).crossfade(true).build(), contentScale = ContentScale.Crop
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .width(50.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .border(1.dp, Color.Gray)
    )
}