package com.cuentasclaras.app.presentation.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuentasclaras.app.domain.usecase.AddExpenseUseCase
import com.cuentasclaras.app.domain.usecase.GetGroupExpensesUseCase
import com.cuentasclaras.app.domain.usecase.GetSessionUseCase
import com.cuentasclaras.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGroupExpensesUseCase: GetGroupExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getSessionUseCase: GetSessionUseCase
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val decodedGroupName: String = Uri.decode(savedStateHandle["groupName"] ?: "Detalle del Grupo")

    private val _uiState = MutableStateFlow(
        GroupDetailUiState(groupName = decodedGroupName)
    )
    val uiState: StateFlow<GroupDetailUiState> = _uiState.asStateFlow()

    init {
        observeUserSession()
        observeLocalExpenses()
        refreshExpenses()
    }

    private fun observeUserSession() {
        viewModelScope.launch {
            getSessionUseCase().collect { session ->
                if (session != null) {
                    _uiState.update { it.copy(defaultPayerName = session.userName) }
                }
            }
        }
    }

    private fun observeLocalExpenses() {
        viewModelScope.launch {
            getGroupExpensesUseCase.observe(groupId).collect { list ->
                _uiState.update { it.copy(expenses = list) }
            }
        }
    }

    fun refreshExpenses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getGroupExpensesUseCase.refresh(groupId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            snackbarMessage = result.warningMessage
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            snackbarMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onOpenAddDialog() {
        _uiState.update { it.copy(isAddDialogOpen = true) }
    }

    fun onDismissAddDialog() {
        if (!_uiState.value.isSavingExpense) {
            _uiState.update { it.copy(isAddDialogOpen = false) }
        }
    }

    fun onAddExpense(title: String, amountText: String, paidBy: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingExpense = true) }
            when (val result = addExpenseUseCase(groupId, title, amountText, paidBy)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSavingExpense = false,
                            isAddDialogOpen = false,
                            snackbarMessage = "Gasto registrado correctamente"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSavingExpense = false,
                            snackbarMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}