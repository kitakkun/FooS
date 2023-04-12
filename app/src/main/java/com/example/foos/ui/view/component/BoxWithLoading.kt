package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex

@Composable
fun BoxWithLoading(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        modifier = modifier,
    ) {
        content()
        if (isLoading) {
            MaxSizeLoadingIndicator(
                modifier = Modifier.zIndex(1f)
            )
        }
    }
}

@Preview
@Composable
private fun BoxWithLoadingPreview() {
    BoxWithLoading(
        isLoading = true,
    ) {
    }
}

@Preview
@Composable
private fun BoxWithNonLoadingPreview() {
    BoxWithLoading(
        isLoading = false,
    ) {
    }
}