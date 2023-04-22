package com.github.kitakkun.foos.post.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.DeletePostByPostIdUseCase
import com.github.kitakkun.foos.common.usecase.FetchPostByPostIdUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PostOptionViewModel(
    private val deletePostByPostIdUseCase: DeletePostByPostIdUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val getPostByPostIdUseCase: FetchPostByPostIdUseCase,
) : ViewModel() {

    private val mutableNavUpEvent = MutableSharedFlow<Unit>()
    val navUpEvent: SharedFlow<Unit> = mutableNavUpEvent

    fun onDeleteClick(postId: String) {
        viewModelScope.launch {
            deletePostByPostIdUseCase(postId = postId)
            mutableNavUpEvent.emit(Unit)
        }
    }

}
