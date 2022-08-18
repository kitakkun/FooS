package com.example.foos.di

import com.example.foos.data.domain.GetLatestPostsWithUserUseCase
import com.example.foos.data.repository.PostsRepository
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
    fun providePostsRepository() : PostsRepository = PostsRepository
    @Provides
    @Singleton
    fun provideUsersRepository() : UsersRepository = UsersRepository
    @Provides
    fun provideCompletePostsRepository() : GetLatestPostsWithUserUseCase = GetLatestPostsWithUserUseCase(PostsRepository, UsersRepository)
}