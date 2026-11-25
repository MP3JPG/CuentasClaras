package com.cuentasclaras.app.domain.model

data class ExpenseGroup(
    val id: String,
    val name: String,
    val description: String,
    val membersCount: Int,
    val userBalance: Double,
    val updatedAt: String
)