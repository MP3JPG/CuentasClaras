package com.cuentasclaras.app.domain.usecase

import com.cuentasclaras.app.domain.model.Expense
import com.cuentasclaras.app.domain.repository.ExpenseRepository
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    fun observe(groupId: String): Flow<List<Expense>> {
        return expenseRepository.observeExpensesByGroup(groupId)
    }

    suspend fun refresh(groupId: String): Resource<Unit> {
        return expenseRepository.syncExpensesByGroup(groupId)
    }
}