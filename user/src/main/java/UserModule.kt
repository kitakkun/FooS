package com.github.kitakkun.foos.user

import com.github.kitakkun.foos.user.auth.signin.SignInViewModel
import com.github.kitakkun.foos.user.auth.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
}
