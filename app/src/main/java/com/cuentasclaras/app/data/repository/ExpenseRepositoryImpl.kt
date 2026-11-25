package com.cuentasclaras.app.data.repository

import com.cuentasclaras.app.data.local.dao.ExpenseDao
import com.cuentasclaras.app.data.local.dao.GroupDao
import com.cuentasclaras.app.data.remote.api.CuentasClarasApi
import com.cuentasclaras.app.data.remote.dto.CreateExpenseRequestDto
import com.cuentasclaras.app.domain.model.Expense
import com.cuentasclaras.app.domain.model.ExpenseGroup
import com.cuentasclaras.app.domain.repository.ExpenseRepository
import com.cuentasclaras.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val api: CuentasClarasApi,
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun observeGroups(): Flow<List<ExpenseGroup>> {
        return groupDao.observeAllGroups().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncGroups(): Resource<Unit> {
        return try {
            val remoteGroups = api.getGroups()
            groupDao.insertGroups(remoteGroups.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Resource.Error("Tu sesión expiró. Vuelve a iniciar sesión.")
            } else {
                Resource.Success(
                    data = Unit,
                    warningMessage = "Servidor no disponible (${e.code()}). Mostrando datos locales."
                )
            }
        } catch (e: IOException) {
            Resource.Success(
                data = Unit,
                warningMessage = "Sin conexión a internet. Mostrando datos guardados localmente."
            )
        } catch (e: Exception) {
            Resource.Success(
                data = Unit,
                warningMessage = "Modo sin conexión activo. Mostrando información local."
            )
        }
    }

    override fun observeExpensesByGroup(groupId: String): Flow<List<Expense>> {
        return expenseDao.observeExpensesByGroup(groupId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncExpensesByGroup(groupId: String): Resource<Unit> {
        return try {
            val remoteExpenses = api.getGroupExpenses(groupId)
            expenseDao.deleteExpensesByGroup(groupId)
            expenseDao.insertExpenses(remoteExpenses.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Success(
                data = Unit,
                warningMessage = "Sin conexión. Mostrando gastos almacenados en el dispositivo."
            )
        } catch (e: HttpException) {
            Resource.Success(
                data = Unit,
                warningMessage = "Error de sincronización (${e.code()}). Mostrando caché local."
            )
        } catch (e: Exception) {
            Resource.Success(
                data = Unit,
                warningMessage = "Mostrando historial local sin conexión."
            )
        }
    }

    override suspend fun addExpense(
        groupId: String,
        title: String,
        amount: Double,
        paidBy: String
    ): Resource<Expense> {
        return try {
            val request = CreateExpenseRequestDto(
                title = title,
                amount = amount,
                paidBy = paidBy
            )
            val createdDto = api.createExpense(groupId, request)
            expenseDao.insertExpense(createdDto.toEntity())

            // Actualizamos también los saldos de los grupos en Room
            try {
                val updatedGroups = api.getGroups()
                groupDao.insertGroups(updatedGroups.map { it.toEntity() })
            } catch (_: Exception) {
                // Si falla la recarga secundaria del saldo, el gasto ya quedó guardado
            }

            Resource.Success(createdDto.toDomain())
        } catch (e: IOException) {
            Resource.Error("Sin conexión a internet. Conéctate para registrar un nuevo gasto.")
        } catch (e: HttpException) {
            Resource.Error("No se pudo registrar el gasto en el servidor (HTTP ${e.code()}).")
        } catch (e: Exception) {
            Resource.Error("Error al guardar el gasto: ${e.localizedMessage ?: "intenta nuevamente"}")
        }
    }
}