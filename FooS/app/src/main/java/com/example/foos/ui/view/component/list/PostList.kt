package com.example.foos.ui.view.component.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.view.component.OnAppearLastItem

/**
 * 投稿のリスト
 * @param uiStates 各投稿のUI状態
 * @param onUserIconClick ユーザアイコンクリック時の挙動
 * @param onContentClick コンテンツクリック時の挙動
 * @param onImageClick 添付画像クリック時の挙動
 */
@Composable
fun PostItemList(
    listState: LazyListState = rememberLazyListState(),
    uiStates: List<PostItemUiState>,
    onUserIconClick: (String) -> Unit = { },
    onContentClick: (String) -> Unit = { },
    onImageClick: (List<String>, String) -> Unit = { _, _ -> },
    onAppearLastItem: (Int) -> Unit = {},
) {
    listState.OnAppearLastItem(onAppearLastItem = onAppearLastItem)

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(uiStates) { post ->
            PostItem(post, onUserIconClick, onContentClick, onImageClick)
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}
