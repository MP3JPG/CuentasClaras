package com.cuentasclaras.app.data.repository

import com.cuentasclaras.app.data.local.dao.ExpenseDao
import com.cuentasclaras.app.data.local.dao.GroupDao
import com.cuentasclaras.app.data.local.datastore.SessionPreferences
import com.cuentasclaras.app.data.remote.api.CuentasClarasApi
import com.cuentasclaras.app.data.remote.dto.LoginRequestDto
import com.cuentasclaras.app.domain.model.UserSession
import com.cuentasclaras.app.domain.repository.AuthRepository
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: CuentasClarasApi,
    private val sessionPreferences: SessionPreferences,
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao
) : AuthRepository {

    override fun observeUserSession(): Flow<UserSession?> {
        return sessionPreferences.userSessionFlow
    }

    override suspend fun login(email: String, password: String): Resource<UserSession> {
        return try {
            val response = api.login(LoginRequestDto(email = email, password = password))
            val session = response.toDomain()
            sessionPreferences.saveSession(session)
            Resource.Success(session)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Resource.Error("Correo o contraseña incorrectos.")
            } else {
                Resource.Error("Error del servidor (HTTP ${e.code()}). Intenta más tarde.")
            }
        } catch (e: IOException) {
            Resource.Error("Sin conexión con el servidor. Verifica tu internet o que la API esté encendida.")
        } catch (e: Exception) {
            Resource.Error("Error inesperado al iniciar sesión: ${e.localizedMessage ?: "desconocido"}")
        }
    }

    override suspend fun logout() {
        sessionPreferences.clearSession()
        groupDao.clearAllGroups()
        expenseDao.clearAllExpenses()
    }
}