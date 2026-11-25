package com.cuentasclaras.app.domain.usecase

import com.cuentasclaras.app.domain.model.ExpenseGroup
import com.cuentasclaras.app.domain.repository.ExpenseRepository
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    fun observe(): Flow<List<ExpenseGroup>> {
        return expenseRepository.observeGroups()
    }

    suspend fun refresh(): Resource<Unit> {
        return expenseRepository.syncGroups()
    }
}