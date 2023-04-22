package com.github.kitakkun.foos.common.di

import com.github.kitakkun.foos.common.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { FetchPostByPostIdUseCase(get(), get(), get()) }
    factory { FetchPostsByLocationBoundsUseCase(get(), get(), get()) }
    factory { FetchPostByDatabasePostUseCase(get(), get()) }
    factory { FetchPostsUserReactedByUserIdUseCase(get(), get()) }
    factory { FetchPostsWithMediaByUserIdUseCase(get(), get(), get()) }
    factory { FetchReactionsByUserIdUseCase(get(), get(), get()) }
    factory { FetchPostsUseCase(get(), get(), get()) }
    factory { FetchFollowStateUseCase(get()) }
    factory { DeletePostByPostIdUseCase(get(), get()) }
    factory { FetchPostsByUserIdUseCase(get(), get(), get()) }
}
