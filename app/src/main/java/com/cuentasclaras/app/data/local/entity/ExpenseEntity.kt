package com.cuentasclaras.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cuentasclaras.app.domain.model.Expense

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val title: String,
    val amount: Double,
    val paidBy: String,
    val date: String
) {
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