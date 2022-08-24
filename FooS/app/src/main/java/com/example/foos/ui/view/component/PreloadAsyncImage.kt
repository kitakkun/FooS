package com.example.foos.ui.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest

/**
 * プリロードを行う非同期読み込み画像
 * @param url 読み込む画像のURL
 * @param model AsyncImageのmodel
 * @param modifier 適用するモディファイア
 * @param contentScale AsyncImageのcontentScale
 * @param contentDescription AsyncImageのcontentDescription
 */
@Composable
fun PreloadAsyncImage(
    url: String,
    model: ImageRequest,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
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
