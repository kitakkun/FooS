package com.github.kitakkun.foos.post.create.locationconfirm

import kotlinx.coroutines.flow.SharedFlow

interface LocationConfirmViewModel {
    val cancelEvent: SharedFlow<Unit>
    val completeEvent: SharedFlow<Unit>
    fun backToPrevScreen()
    fun completeStep()
}
