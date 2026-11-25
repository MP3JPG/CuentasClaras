package com.cuentasclaras.app.domain.usecase

import com.cuentasclaras.app.domain.model.UserSession
import com.cuentasclaras.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserSession?> {
        return authRepository.observeUserSession()
    }
}