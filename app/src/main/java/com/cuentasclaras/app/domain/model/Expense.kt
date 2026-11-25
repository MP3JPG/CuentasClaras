package com.cuentasclaras.app.domain.model

data class Expense(
    val id: String,
    val groupId: String,
    val title: String,
    val amount: Double,
    val paidBy: String,
    val date: String
)