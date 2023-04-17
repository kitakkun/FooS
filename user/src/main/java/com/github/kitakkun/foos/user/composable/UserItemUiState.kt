package com.github.kitakkun.foos.user.composable

import com.github.kitakkun.foos.common.model.DatabaseUser
import com.github.kitakkun.foos.common.model.FollowState

data class UserItemUiState(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val biography: String,
    val isFollowedByClient: Boolean,
    val isFollowsYouVisible: Boolean = false,
    val isFollowButtonVisible: Boolean = false,
) {
    companion object {
        fun convert(
            databaseUser: DatabaseUser,
            followState: FollowState,
            isClientUser: Boolean
        ) = UserItemUiState(
            isFollowButtonVisible = isClientUser,
            name = databaseUser.name,
            id = databaseUser.id,
            profileImageUrl = databaseUser.profileImage,
            biography = "",
            isFollowedByClient = followState.following,
            isFollowsYouVisible = followState.followed,
        )

        fun buildTestData(
            id: String = "id",
            name: String = "name",
        ) = UserItemUiState(
            id = id,
            name = name,
            profileImageUrl = "",
            biography = "biography",
            isFollowedByClient = true,
            isFollowsYouVisible = true,
            isFollowButtonVisible = true,
        )
    }
}
