package com.github.kitakkun.foos.common.di

import com.github.kitakkun.foos.common.di.FirebaseModule.provideFirebaseAuthInstance
import com.github.kitakkun.foos.common.di.RepositoryModule.provideFollowRepository
import com.github.kitakkun.foos.common.di.RepositoryModule.providePostsRepository
import com.github.kitakkun.foos.common.di.RepositoryModule.provideReactionsRepository
import com.github.kitakkun.foos.common.di.RepositoryModule.provideUsersRepository
import com.github.kitakkun.foos.common.usecase.*
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
