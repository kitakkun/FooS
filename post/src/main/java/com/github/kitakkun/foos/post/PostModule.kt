package com.github.kitakkun.foos.post

import com.github.kitakkun.foos.post.bottomsheet.PostOptionViewModel
import com.github.kitakkun.foos.post.create.PostCreateViewModel
import com.github.kitakkun.foos.post.postdetail.PostDetailViewModel
import com.github.kitakkun.foos.post.reaction.ReactionViewModel
import com.github.kitakkun.foos.post.timeline.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postModule = module {
    viewModel { PostDetailViewModel(get(), get()) }
    viewModel { ReactionViewModel(get()) }
    viewModel { PostCreateViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { PostOptionViewModel(get(), get(), get()) }
}
