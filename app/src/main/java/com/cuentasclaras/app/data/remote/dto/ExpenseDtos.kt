package com.cuentasclaras.app.data.remote.dto

import com.cuentasclaras.app.data.local.entity.ExpenseEntity
import com.cuentasclaras.app.domain.model.Expense

data class CreateExpenseRequestDto(
    val title: String,
    val amount: Double,
    val paidBy: String
)

data class ExpenseDto(
    val id: String,
    val groupId: String,
    val title: String,
    val amount: Double,
    val paidBy: String,
    val date: String
) {
    fun toEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            groupId = groupId,
            title = title,
            amount = amount,
            paidBy = paidBy,
            date = date
        )
    }

    fun toDomain(): Expense {
        return Expense(
            id = id,
            groupId = groupId,
            title = title,
            amount = amount,
            paidBy = paidBy,
            date = date
        )
    }
}