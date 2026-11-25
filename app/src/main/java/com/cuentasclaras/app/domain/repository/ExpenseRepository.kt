package com.cuentasclaras.app.domain.repository

import com.cuentasclaras.app.domain.model.Expense
import com.cuentasclaras.app.domain.model.ExpenseGroup
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeGroups(): Flow<List<ExpenseGroup>>
    suspend fun syncGroups(): Resource<Unit>
    fun observeExpensesByGroup(groupId: String): Flow<List<Expense>>
    suspend fun syncExpensesByGroup(groupId: String): Resource<Unit>
    suspend fun addExpense(
        groupId: String,
        title: String,
        amount: Double,
        paidBy: String
    ): Resource<Expense>
}