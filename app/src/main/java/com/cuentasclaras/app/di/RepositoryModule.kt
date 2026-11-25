package com.cuentasclaras.app.di

import com.cuentasclaras.app.data.repository.AuthRepositoryImpl
import com.cuentasclaras.app.data.repository.ExpenseRepositoryImpl
import com.cuentasclaras.app.domain.repository.AuthRepository
import com.cuentasclaras.app.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl
    ): ExpenseRepository
}