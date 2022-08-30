package com.example.foos.ui.navigation.navargs

import android.os.Parcelable
import com.example.foos.ui.state.screen.home.PostItemUiState
import kotlinx.parcelize.Parcelize

/**
 * クリックされた画像URLと投稿のUI状態を同時に渡すためのデータクラス
 * @param uiState 投稿のUI状態
 * @param index クリックされた画像のURL
 */
@Parcelize
data class PostItemUiStateWithImageUrl(
    val uiState: PostItemUiState,
    val index: Int,
) : Parcelable
