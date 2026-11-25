package com.cuentasclaras.app.presentation.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuentasclaras.app.domain.usecase.GetGroupsUseCase
import com.cuentasclaras.app.domain.usecase.GetSessionUseCase
import com.cuentasclaras.app.domain.usecase.LogoutUseCase
import com.cuentasclaras.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState: StateFlow<GroupsUiState> = _uiState.asStateFlow()

    init {
        observeSession()
        observeLocalGroups()
        refreshGroups()
    }

    private fun observeSession() {
        viewModelScope.launch {
            getSessionUseCase().collect { session ->
                if (session != null) {
                    _uiState.update { it.copy(userName = session.userName) }
                }
            }
        }
    }

    private fun observeLocalGroups() {
        viewModelScope.launch {
            getGroupsUseCase.observe().collect { groupList ->
                _uiState.update { it.copy(groups = groupList) }
            }
        }
    }

    fun refreshGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getGroupsUseCase.refresh()) {
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

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}