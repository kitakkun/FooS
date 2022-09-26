package com.example.foos.ui.view.screen.setting

data class SettingUiState(
    val username: String,
    val profileImage: String,
) {
    companion object {
        val Default = SettingUiState(
            username = "",
            profileImage = "",
        )
    }
}
