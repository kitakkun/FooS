package com.example.foos.di

import com.example.foos.data.domain.FetchReactionsByUserIdUseCase
import com.example.foos.data.domain.converter.uistate.ConvertPostToUiStateUseCase
import com.example.foos.data.domain.converter.uistate.ConvertReactionToUiStateUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFolloweesWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFollowersWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.post.*
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

    /**
     * Provide Repositories...
     */
    @Provides
    @Singleton
    fun providePostsRepository(): PostsRepository = PostsRepository

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository = UsersRepository

    @Provides
    @Singleton
    fun provideReactionsRepository(): ReactionsRepository = ReactionsRepository

    @Provides
    @Singleton
    fun provideFollowRepository(): FollowRepository = FollowRepository

    /**
     * Provide UseCases...
     */
    @Provides
    fun provideFetchPostByPostIdUseCase(): FetchPostByPostIdUseCase = FetchPostByPostIdUseCase(
        providePostsRepository(),
        provideUsersRepository(),
        provideReactionsRepository(),
    )

    @Provides
    fun provideFetchPostsByLocationBoundsUseCase(): FetchPostsByLocationBoundsUseCase =
        FetchPostsByLocationBoundsUseCase(
            providePostsRepository(), provideUsersRepository(), provideReactionsRepository(),
        )

    @Provides
    fun providePostToUiStateUseCase(): ConvertPostToUiStateUseCase = ConvertPostToUiStateUseCase()

    @Provides
    fun provideFetchPostByDatabasePostUseCase(): FetchPostByDatabasePostUseCase =
        FetchPostByDatabasePostUseCase(
            provideUsersRepository(),
            provideReactionsRepository(),
        )

    @Provides
    fun provideFetchPostsUserReactedByUserIdUseCase(): FetchPostsUserReactedByUserIdUseCase =
        FetchPostsUserReactedByUserIdUseCase(
            provideReactionsRepository(), provideFetchPostByPostIdUseCase()
        )

    @Provides
    fun provideFetchPostsWithMediaByUserIdUseCase(): FetchPostsWithMediaByUserIdUseCase =
        FetchPostsWithMediaByUserIdUseCase(
            providePostsRepository(), provideUsersRepository(), provideReactionsRepository()
        )


    @Provides
    fun provideFetchReactionsByUserIdUseCase(): FetchReactionsByUserIdUseCase =
        FetchReactionsByUserIdUseCase(UsersRepository, PostsRepository, ReactionsRepository)

    @Provides
    fun provideConvertReactionToUiStateUseCase(): ConvertReactionToUiStateUseCase =
        ConvertReactionToUiStateUseCase()

    @Provides
    fun provideFetchFollowersWithMyFollowStateByUserIdUseCase(): FetchFollowersWithMyFollowStateByUserIdUseCase =
        FetchFollowersWithMyFollowStateByUserIdUseCase(
            provideUsersRepository(), provideFollowRepository()
        )

    @Provides
    fun provideFetchFolloweesWithMyFollowStateByUserIdUseCase(): FetchFolloweesWithMyFollowStateByUserIdUseCase =
        FetchFolloweesWithMyFollowStateByUserIdUseCase(
            provideUsersRepository(), provideFollowRepository()
        )

    @Provides
    fun provideFetchPostsUseCase(): FetchPostsUseCase = FetchPostsUseCase(
        providePostsRepository(), provideUsersRepository(), provideReactionsRepository()
    )
}