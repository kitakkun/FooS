package com.github.kitakkun.foos.user.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.common.const.paddingLarge
import com.github.kitakkun.foos.common.ext.OnAppearLastItem
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState

/**
 * プロフィール画面の画像コンテンツベースの投稿グリッドビュー
 * @param uiStates 表示する投稿のUI状態のリスト
 * @param onContentClick コンテンツがクリックされたときの挙動
 */
@Composable
fun MediaPostGrid(
    uiStates: List<PostItemUiState>,
    onContentClick: (String) -> Unit = {},
    onAppearLastItem: () -> Unit = {},
) {
    val gridState = rememberLazyGridState()
    gridState.OnAppearLastItem(onAppearLastItem = { onAppearLastItem.invoke() })
    if (uiStates.isEmpty()) {
        Text(
            text = "No posts available.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(paddingLarge)
                .fillMaxWidth()
        )
    } else {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 128.dp),
        ) {
            items(uiStates) {
                MediaPostItem(it, onContentClick = onContentClick)
            }
        }
    }
}
