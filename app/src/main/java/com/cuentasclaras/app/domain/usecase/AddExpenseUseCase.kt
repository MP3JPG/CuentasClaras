package com.cuentasclaras.app.domain.usecase

import com.cuentasclaras.app.domain.model.Expense
import com.cuentasclaras.app.domain.repository.ExpenseRepository
import com.cuentasclaras.app.domain.util.Resource
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(
        groupId: String,
        title: String,
        amountText: String,
        paidBy: String
    ): Resource<Expense> {
        if (title.isBlank()) {
            return Resource.Error("Escribe el concepto del gasto.")
        }
        val parsedAmount = amountText.toDoubleOrNull()
        if (parsedAmount == null || parsedAmount <= 0.0) {
            return Resource.Error("Ingresa un monto válido mayor a 0.")
        }
        if (paidBy.isBlank()) {
            return Resource.Error("Indica quién realizó el pago.")
        }
        return expenseRepository.addExpense(
            groupId = groupId,
            title = title.trim(),
            amount = parsedAmount,
            paidBy = paidBy.trim()
        )
    }
}