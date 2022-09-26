package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.foos.ui.theme.FooSTheme

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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MaxSizeLoadingIndicatorPreview() {
    FooSTheme {
        MaxSizeLoadingIndicator(isLoading = true)
    }
}