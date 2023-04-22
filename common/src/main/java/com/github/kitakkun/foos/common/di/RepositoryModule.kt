package com.github.kitakkun.foos.common.di

import com.github.kitakkun.foos.common.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<PostsRepository> { PostsRepositoryImpl(get(), get()) }
    single<UsersRepository> { UsersRepositoryImpl(get(), get()) }
    single<ReactionsRepository> { ReactionsRepositoryImpl(get()) }
    single<FollowRepository> { FollowRepositoryImpl(get(), get()) }
}
