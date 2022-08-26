package com.example.foos.data.domain

import com.example.foos.data.model.PostWithUser
import com.example.foos.ui.state.screen.home.PostItemUiState

class ConvertPostWithUserToUiStateUseCase {

    operator fun invoke(postWithUser: PostWithUser) : PostItemUiState {
        return PostItemUiState(
                postId = postWithUser.databasePost.postId,
                userId = postWithUser.databaseUser.userId,
                username = postWithUser.databaseUser.username,
                userIcon = postWithUser.databaseUser.profileImage,
                content = postWithUser.databasePost.content,
                attachedImages = postWithUser.databasePost.attachedImages,
                latitude = postWithUser.databasePost.latitude,
                longitude = postWithUser.databasePost.longitude,
                createdAt = postWithUser.databasePost.createdAt,
            )
        }
    }