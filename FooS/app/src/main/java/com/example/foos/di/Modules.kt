package com.example.foos.di

import com.example.foos.data.domain.FetchReactionsByUserIdUseCase
import com.example.foos.data.domain.converter.uistate.ConvertPostToUiStateUseCase
import com.example.foos.data.domain.converter.uistate.ConvertReactionToUiStateUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFolloweesWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFollowersWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.post.*
import com.example.foos.data.repository.UsersRepository
import com.example.foos.di.RepositoryModule.provideFollowRepository
import com.example.foos.di.RepositoryModule.providePostsRepository
import com.example.foos.di.RepositoryModule.provideReactionsRepository
import com.example.foos.di.RepositoryModule.provideUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Modules {

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
        FetchReactionsByUserIdUseCase(
            UsersRepository,
            providePostsRepository(),
            provideReactionsRepository()
        )

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