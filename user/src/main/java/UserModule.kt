package com.github.kitakkun.foos.user

import com.github.kitakkun.foos.user.auth.signin.SignInViewModel
import com.github.kitakkun.foos.user.auth.signup.SignUpViewModel
import com.github.kitakkun.foos.user.followlist.FollowListViewModel
import com.github.kitakkun.foos.user.profile.UserProfileViewModel
import com.github.kitakkun.foos.user.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (userId: String, shouldShowFollowingListFirst: Boolean) ->
        FollowListViewModel(
            userId = userId,
            shouldShowFollowingListFirst = shouldShowFollowingListFirst,
            auth = get(),
            usersRepository = get(),
            followRepository = get(),
            fetchFollowStateUseCase = get()
        )
    }
    viewModel { (userId: String) ->
        UserProfileViewModel(
            userId, get(), get(), get(), get(), get()
        )
    }
    viewModel { SettingViewModel(get(), get()) }
}
