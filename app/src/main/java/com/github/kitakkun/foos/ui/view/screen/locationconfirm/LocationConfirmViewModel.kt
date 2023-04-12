package com.github.kitakkun.foos.ui.view.screen.locationconfirm

import kotlinx.coroutines.flow.SharedFlow

interface LocationConfirmViewModel {
    val cancelEvent: SharedFlow<Unit>
    val completeEvent: SharedFlow<Unit>
    fun backToPrevScreen()
    fun completeStep()
}
