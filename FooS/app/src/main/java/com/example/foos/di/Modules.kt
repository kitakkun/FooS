package com.example.foos.di

import com.example.foos.data.domain.fetcher.FetchPostByPostIdUseCase
import com.example.foos.data.domain.GetPostsWithUserByUserIdWithDateUseCase
import com.example.foos.data.domain.GetPostsWithUserUseCase
import com.example.foos.data.domain.GetReactionsByUserIdUseCase
import com.example.foos.data.domain.converter.uistate.ConvertPostToUiStateUseCase
import com.example.foos.data.domain.converter.uistate.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.converter.uistate.ConvertReactionToUiStateUseCase
import com.example.foos.data.domain.fetcher.FetchFolloweesWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.FetchFollowersWithMyFollowStateByUserIdUseCase
import com.example.foos.data.domain.fetcher.FetchPostsByLocationBoundsUseCase
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
    fun provideGetPostByPostIdUseCase(): FetchPostByPostIdUseCase = FetchPostByPostIdUseCase(
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
    fun provideCompletePostsRepository(): GetPostsWithUserUseCase =
        GetPostsWithUserUseCase(PostsRepository, UsersRepository)

    @Provides
    fun provideGetReactionsByUserIdUseCase(): GetReactionsByUserIdUseCase =
        GetReactionsByUserIdUseCase(UsersRepository, PostsRepository, ReactionsRepository)

    @Provides
    fun provideGetPostsWithUserByUserIdWithDateUseCase(): GetPostsWithUserByUserIdWithDateUseCase =
        GetPostsWithUserByUserIdWithDateUseCase(PostsRepository, UsersRepository)

    @Provides
    fun provideConvertReactionToUiStateUseCase(): ConvertReactionToUiStateUseCase =
        ConvertReactionToUiStateUseCase()

    @Provides
    fun provideConvertPostWithUserToUiStateUseCase(): ConvertPostWithUserToUiStateUseCase =
        ConvertPostWithUserToUiStateUseCase()

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
}