package com.example.foos.ui.view.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.DeletePostByPostIdUseCase
import com.example.foos.data.domain.fetcher.post.FetchPostByPostIdUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostOptionViewModelImpl @Inject constructor(
    private val deletePostByPostIdUseCase: DeletePostByPostIdUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val getPostByPostIdUseCase: FetchPostByPostIdUseCase,
) : ViewModel(), PostOptionViewModel {

    override fun onDeleteClick(postId: String) {
        viewModelScope.launch {
            deletePostByPostIdUseCase(postId = postId)
        }
    }

}
