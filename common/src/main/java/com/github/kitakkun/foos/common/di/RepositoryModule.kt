package com.github.kitakkun.foos.common.di

import com.github.kitakkun.foos.common.di.FirebaseModule.provideFireStoreInstance
import com.github.kitakkun.foos.common.di.FirebaseModule.provideFirebaseAuthInstance
import com.github.kitakkun.foos.common.di.FirebaseModule.provideFirebaseStorageInstance
import com.github.kitakkun.foos.common.repository.*
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
    fun providePostsRepository(): PostsRepository =
        PostsRepositoryImpl(provideFireStoreInstance(), provideFirebaseStorageInstance())

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository =
        UsersRepositoryImpl(provideFireStoreInstance(), provideFirebaseAuthInstance())

    @Provides
    @Singleton
    fun provideReactionsRepository(): ReactionsRepository =
        ReactionsRepositoryImpl(provideFireStoreInstance())

    @Provides
    @Singleton
    fun provideFollowRepository(): FollowRepository =
        FollowRepositoryImpl(provideFirebaseAuthInstance(), provideFireStoreInstance())
}