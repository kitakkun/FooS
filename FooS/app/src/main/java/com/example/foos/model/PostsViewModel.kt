package com.example.foos.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostsViewModel : ViewModel() {

    private val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().also {
            loadPosts()
        }
    }

    fun getPosts(): LiveData<List<Post>> {
        return posts
    }

    fun loadPosts() {

    }
}