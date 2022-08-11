package com.example.foos.ui.composable.component

import android.os.Debug
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.foos.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
    val storageReference = Firebase.storage.reference.child("images/profile/"+ Firebase.auth.uid + ".png")
    var url by remember { mutableStateOf("") }
    storageReference.downloadUrl.addOnSuccessListener {
        url = it.toString()
        Log.d("TAG", url)
    }
    val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
        .data(url).crossfade(true).build()
        , contentScale = ContentScale.Crop)
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