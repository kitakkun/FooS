package com.github.kitakkun.foos.ui.bottomsheet

import kotlinx.coroutines.flow.SharedFlow

interface PostOptionViewModel {
    val navUpEvent: SharedFlow<Unit>
    fun onDeleteClick(postId: String)
}
