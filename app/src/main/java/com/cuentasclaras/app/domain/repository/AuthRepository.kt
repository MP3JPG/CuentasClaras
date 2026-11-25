package com.cuentasclaras.app.domain.repository

import com.cuentasclaras.app.domain.model.UserSession
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeUserSession(): Flow<UserSession?>
    suspend fun login(email: String, password: String): Resource<UserSession>
    suspend fun logout()
}