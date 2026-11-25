package com.cuentasclaras.app.domain.usecase

import com.cuentasclaras.app.domain.model.UserSession
import com.cuentasclaras.app.domain.repository.AuthRepository
import com.cuentasclaras.app.domain.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<UserSession> {
        if (email.isBlank() || !email.contains("@")) {
            return Resource.Error("Ingresa un correo electrónico válido.")
        }
        if (password.length < 4) {
            return Resource.Error("La contraseña debe tener al menos 4 caracteres.")
        }
        return authRepository.login(email.trim(), password.trim())
    }
}