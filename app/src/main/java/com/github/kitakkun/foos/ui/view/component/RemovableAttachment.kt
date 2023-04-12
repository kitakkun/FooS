package com.github.kitakkun.foos.ui.view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.kitakkun.foos.R
import com.github.kitakkun.foos.customview.preview.PreviewContainer

/**
 * アニメーション付き
 */
@Composable
private fun AnimatedRemovableAttachment(
    modifier: Modifier = Modifier,
    onCloseButtonClick: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(visibleState = state) {
        RemovableAttachment(
            modifier = modifier,
            onCloseButtonClick = onCloseButtonClick,
            content = content
        )
    }
}

/**
 * 投稿画面のアタッチメント
 */
@Composable
private fun RemovableAttachment(
    modifier: Modifier = Modifier,
    onCloseButtonClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
            .size(128.dp)
            .clip(RoundedCornerShape(10))
    ) {
        content()
        CloseButton(onClick = onCloseButtonClick)
    }
}

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(1.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.surface.copy(alpha = 0.4f)),
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "remove attachment",
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun ImageAttachment(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onCloseButtonClick: () -> Unit
) {
    RemovableAttachment(
        modifier = modifier,
        onCloseButtonClick = onCloseButtonClick,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).build(),
            loading = {
                MaxSizeLoadingIndicator()
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun LocationAttachment(
    onCloseButtonClick: () -> Unit,
) {
    RemovableAttachment(
        onCloseButtonClick = onCloseButtonClick,
        modifier = Modifier.background(MaterialTheme.colors.surface.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pin_drop),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "location attachment"
            )
        }
    }
}

@Preview
@Composable
private fun ImageAttachmentPreview() = PreviewContainer {
    ImageAttachment(imageUrl = "", onCloseButtonClick = {})
}

@Preview
@Composable
private fun LocationAttachmentPreview() = PreviewContainer {
    LocationAttachment(onCloseButtonClick = {})
}
