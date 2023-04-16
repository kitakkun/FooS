package com.github.kitakkun.foos.user.setting

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
