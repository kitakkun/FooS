package com.example.foos.ui.view.screen.followlist

import androidx.compose.runtime.State
import com.example.foos.ui.state.screen.followlist.FollowListScreenUiState
import kotlinx.coroutines.flow.SharedFlow

interface FollowListViewModel {
    val uiState: State<FollowListScreenUiState>
    val navEvent: SharedFlow<String>
    fun navigateToUserProfile(userId: String)
    fun fetchFollowees(userId: String)
    fun fetchFollowers(userId: String)
}
