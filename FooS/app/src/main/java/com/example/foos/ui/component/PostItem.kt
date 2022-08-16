package com.example.foos.ui.component

import android.os.Debug
import android.util.Log
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.foos.ui.home.ReactionRow
import com.example.foos.ui.home.PostItemUiState

/**
 * タイムラインに表示する投稿アイテム一つのコンポーザブル
 */
@Preview(showBackground = true)
@Composable
fun PostItem(
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = postItemUiState.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "@${postItemUiState.userId}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image).crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null
            )
        }
    }
}