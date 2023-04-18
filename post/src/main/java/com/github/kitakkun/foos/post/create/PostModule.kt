package com.github.kitakkun.foos.post.create

import com.github.kitakkun.foos.common.di.FirebaseModule.provideFirebaseAuthInstance
import com.github.kitakkun.foos.common.di.RepositoryModule.providePostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PostModule {
    @Provides
    fun providePostCreateViewModel() = PostCreateViewModel(
        firebaseAuth = provideFirebaseAuthInstance(),
        postsRepository = providePostsRepository()
    )
}
