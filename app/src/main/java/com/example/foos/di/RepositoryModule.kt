package com.example.foos.di

import com.example.foos.data.repository.*
import com.example.foos.di.FirebaseModule.provideFireStoreInstance
import com.example.foos.di.FirebaseModule.provideFirebaseAuthInstance
import com.example.foos.di.FirebaseModule.provideFirebaseStorageInstance
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
