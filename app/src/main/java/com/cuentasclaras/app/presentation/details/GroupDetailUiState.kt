package com.cuentasclaras.app.presentation.detail

import com.cuentasclaras.app.domain.model.Expense

data class GroupDetailUiState(
    val groupName: String = "",
    val defaultPayerName: String = "Isaac",
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val isAddDialogOpen: Boolean = false,
    val isSavingExpense: Boolean = false,
    val snackbarMessage: String? = null
)