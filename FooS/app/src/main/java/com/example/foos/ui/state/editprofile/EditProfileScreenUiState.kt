package com.example.foos.ui.state.editprofile

data class EditProfileScreenUiState(
    val username: String,
    val profileImage: String,
    val bio: String,
) {
    companion object {
        val Default = EditProfileScreenUiState(
            username = "username",
            profileImage = "",
            bio = "some biography..."
        )
    }
}
