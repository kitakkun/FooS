package com.github.kitakkun.foos.di

import com.github.kitakkun.foos.data.domain.DeletePostByPostIdUseCase
import com.github.kitakkun.foos.data.domain.FetchReactionsByUserIdUseCase
import com.github.kitakkun.foos.data.domain.fetcher.follow.FetchFollowStateUseCase
import com.github.kitakkun.foos.data.domain.fetcher.post.*
import com.github.kitakkun.foos.di.FirebaseModule.provideFirebaseAuthInstance
import com.github.kitakkun.foos.di.RepositoryModule.provideFollowRepository
import com.github.kitakkun.foos.di.RepositoryModule.providePostsRepository
import com.github.kitakkun.foos.di.RepositoryModule.provideReactionsRepository
import com.github.kitakkun.foos.di.RepositoryModule.provideUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

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
    fun provideFetchPostsUseCase(): FetchPostsUseCase = FetchPostsUseCase(
        providePostsRepository(), provideUsersRepository(), provideReactionsRepository()
    )

    @Provides
    fun provideFetchFollowStateUseCase(): FetchFollowStateUseCase = FetchFollowStateUseCase(
        provideFollowRepository()
    )

    @Provides
    fun provideDeletePostByPostIdUseCase(): DeletePostByPostIdUseCase = DeletePostByPostIdUseCase(
        postsRepository = providePostsRepository(),
        firebaseAuth = provideFirebaseAuthInstance()
    )
}
