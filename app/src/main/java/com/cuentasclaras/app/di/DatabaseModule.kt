package com.cuentasclaras.app.di

import android.content.Context
import androidx.room.Room
import com.cuentasclaras.app.data.local.AppDatabase
import com.cuentasclaras.app.data.local.dao.ExpenseDao
import com.cuentasclaras.app.data.local.dao.GroupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cuentas_claras_db"
        ).build()
    }

    @Provides
    fun provideGroupDao(database: AppDatabase): GroupDao {
        return database.groupDao()
    }

    @Provides
    fun provideExpenseDao(database: AppDatabase): ExpenseDao {
        return database.expenseDao()
    }
}