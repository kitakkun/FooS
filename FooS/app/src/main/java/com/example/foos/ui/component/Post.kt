package com.example.foos.ui.component

import android.os.Debug
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.ui.home.ReactionRow
import com.example.foos.ui.home.PostItemUiState


@Preview(showBackground = true)
@Composable
fun Post(
    postItemUiState: PostItemUiState = PostItemUiState.Default,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        PostContentRow(postItemUiState)
        ReactionRow(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun PostContentRow(
    postItemUiState: PostItemUiState = PostItemUiState.Default
) {
    Row {
        AsyncUserIcon(url = postItemUiState.userIcon)
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Row() {
                Text(postItemUiState.username)
                Spacer(Modifier.width(24.dp))
                Text(postItemUiState.userId)
            }
            Text(
                text = postItemUiState.content,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            PostImagesRow()
        }
    }
}

@Preview
@Composable
fun PostImagesRow(
    images: List<String> = listOf("https://www.gstatic.com/webp/gallery/4.sm.jpg")
) {
    LazyRow(
    ) {
        items(images) { image ->
            Log.d("IMAGE", image)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true).build(),
                contentDescription = null
            )
        }

    }
}