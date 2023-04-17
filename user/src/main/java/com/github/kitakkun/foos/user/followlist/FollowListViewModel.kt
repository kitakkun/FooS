package com.github.kitakkun.foos.user.followlist

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.SharedFlow

interface FollowListViewModel {
    val uiState: State<FollowListScreenUiState>
    val navEvent: SharedFlow<String>
    fun navigateToUserProfile(userId: String)
    fun fetchFollowingUsers(userId: String)
    fun fetchFollowerUsers(userId: String)
}
