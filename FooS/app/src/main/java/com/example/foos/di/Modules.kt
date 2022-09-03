package com.example.foos.di

import com.example.foos.data.domain.FetchReactionsByUserIdUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFolloweesWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.follow.FetchFollowersWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.post.*
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
            provideUsersRepository(),
            providePostsRepository(),
            provideReactionsRepository()
        )

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