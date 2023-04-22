package com.github.kitakkun.foos

import com.github.kitakkun.foos.common.di.firebaseModule
import com.github.kitakkun.foos.common.di.repositoryModule
import com.github.kitakkun.foos.common.di.useCaseModule
import com.github.kitakkun.foos.user.userModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule,
        repositoryModule,
        userModule,
        useCaseModule,
    )
}
