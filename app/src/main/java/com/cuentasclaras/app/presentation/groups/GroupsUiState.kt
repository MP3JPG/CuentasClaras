package com.cuentasclaras.app.presentation.groups

import com.cuentasclaras.app.domain.model.ExpenseGroup

data class GroupsUiState(
    val userName: String = "Usuario",
    val groups: List<ExpenseGroup> = emptyList(),
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)