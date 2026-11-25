package com.cuentasclaras.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuentasclaras.app.data.local.dao.ExpenseDao
import com.cuentasclaras.app.data.local.dao.GroupDao
import com.cuentasclaras.app.data.local.entity.ExpenseEntity
import com.cuentasclaras.app.data.local.entity.GroupEntity

@Database(
    entities = [GroupEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun expenseDao(): ExpenseDao
}