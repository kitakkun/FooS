package com.github.kitakkun.foos.di

import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.common.repository.PostsRepositoryImpl
import com.github.kitakkun.foos.common.repository.ReactionsRepository
import com.github.kitakkun.foos.common.repository.ReactionsRepositoryImpl
import com.github.kitakkun.foos.di.FirebaseModule.provideFireStoreInstance
import com.github.kitakkun.foos.di.FirebaseModule.provideFirebaseAuthInstance
import com.github.kitakkun.foos.di.FirebaseModule.provideFirebaseStorageInstance
import com.github.kitakkun.foos.user.repository.FollowRepository
import com.github.kitakkun.foos.user.repository.FollowRepositoryImpl
import com.github.kitakkun.foos.user.repository.UsersRepository
import com.github.kitakkun.foos.user.repository.UsersRepositoryImpl
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
