package com.cuentasclaras.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cuentasclaras.app.domain.model.ExpenseGroup

@Entity(tableName = "expense_groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val membersCount: Int,
    val userBalance: Double,
    val updatedAt: String
) {
    fun toDomain(): ExpenseGroup {
        return ExpenseGroup(
            id = id,
            name = name,
            description = description,
            membersCount = membersCount,
            userBalance = userBalance,
            updatedAt = updatedAt
        )
    }
}