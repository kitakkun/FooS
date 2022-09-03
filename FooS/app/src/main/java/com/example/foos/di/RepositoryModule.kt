package com.example.foos.di

import com.example.foos.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provide Repositories...
     */
    @Provides
    @Singleton
    fun providePostsRepository(): PostsRepository = PostsRepositoryImpl()

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository = UsersRepositoryImpl()

    @Provides
    @Singleton
    fun provideReactionsRepository(): ReactionsRepository = ReactionsRepositoryImpl()

    @Provides
    @Singleton
    fun provideFollowRepository(): FollowRepository = FollowRepositoryImpl()
}