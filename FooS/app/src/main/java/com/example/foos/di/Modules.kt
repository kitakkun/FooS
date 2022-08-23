package com.example.foos.di

import com.example.foos.data.domain.GetLatestPostsWithUserUseCase
import com.example.foos.data.domain.GetUserInfoUseCase
import com.example.foos.data.repository.FollowRepository
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.ReactionsRepository
import com.example.foos.data.repository.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providePostsRepository(): PostsRepository = PostsRepository

    @Provides
    @Singleton
    fun provideFollowRepository(): FollowRepository = FollowRepository

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository = UsersRepository

    @Provides
    @Singleton
    fun provideReactionsRepository(): ReactionsRepository = ReactionsRepository

    @Provides
    fun provideCompletePostsRepository(): GetLatestPostsWithUserUseCase =
        GetLatestPostsWithUserUseCase(PostsRepository, UsersRepository)
}