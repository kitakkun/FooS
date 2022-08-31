package com.example.foos.data.domain.converter.uistate

import com.example.foos.data.model.Post
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.view.component.list.PostItem

class ConvertPostToUiStateUseCase {

    operator fun invoke(post: Post) : PostItemUiState {
        return PostItemUiState(
            postId = post.post.postId,
            userIcon = post.user.profileImage,
            username = post.user.username,
            longitude = post.post.longitude,
            latitude = post.post.latitude,
            createdAt = post.post.createdAt,
            content = post.post.content,
            attachedImages = post.post.attachedImages,
            userId = post.user.userId,
            reactions = post.reaction,
        )
    }

}