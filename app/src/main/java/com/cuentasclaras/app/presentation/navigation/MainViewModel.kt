package com.cuentasclaras.app.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuentasclaras.app.domain.usecase.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AuthNavigationState {
    data object Loading : AuthNavigationState
    data object Authenticated : AuthNavigationState
    data object Unauthenticated : AuthNavigationState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    getSessionUseCase: GetSessionUseCase
) : ViewModel() {

    val authState: StateFlow<AuthNavigationState> = getSessionUseCase()
        .map { session ->
            if (session != null) {
                AuthNavigationState.Authenticated
            } else {
                AuthNavigationState.Unauthenticated
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthNavigationState.Loading
        )
}