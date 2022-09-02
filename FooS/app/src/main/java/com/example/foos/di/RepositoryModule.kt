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
    fun provideUsersRepository(): UsersRepository = UsersRepository

    @Provides
    @Singleton
    fun provideReactionsRepository(): ReactionsRepository = ReactionsRepository

    @Provides
    @Singleton
    fun provideFollowRepository(): FollowRepository = FollowRepository
}