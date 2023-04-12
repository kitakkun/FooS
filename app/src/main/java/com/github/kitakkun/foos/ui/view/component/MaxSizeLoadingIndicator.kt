package com.github.kitakkun.foos.ui.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.customview.preview.PreviewContainer

/**
 * 親要素に対してフルサイズでストレッチするローディングインディケータ
 * @param isLoading ローディング中かどうか
 */
@Composable
fun MaxSizeLoadingIndicator(
    isLoading: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun MaxSizeLoadingIndicatorPreview() = PreviewContainer {
    MaxSizeLoadingIndicator(isLoading = true)
}
