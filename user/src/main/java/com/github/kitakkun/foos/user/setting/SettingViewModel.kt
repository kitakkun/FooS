package com.github.kitakkun.foos.user.setting

import androidx.compose.runtime.State
import com.canhub.cropper.CropImageView

interface SettingViewModel {
    val uiState: State<SettingUiState>
    fun fetchUserData()
    fun onCropImageSucceeded(result: CropImageView.CropResult)
    fun onCropImageFailed(result: CropImageView.CropResult)
}