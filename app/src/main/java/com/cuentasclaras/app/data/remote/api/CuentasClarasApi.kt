package com.cuentasclaras.app.data.remote.api

import com.cuentasclaras.app.data.remote.dto.CreateExpenseRequestDto
import com.cuentasclaras.app.data.remote.dto.ExpenseDto
import com.cuentasclaras.app.data.remote.dto.GroupDto
import com.cuentasclaras.app.data.remote.dto.LoginRequestDto
import com.cuentasclaras.app.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CuentasClarasApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("groups/{groupId}/expenses")
    suspend fun getGroupExpenses(
        @Path("groupId") groupId: String
    ): List<ExpenseDto>

    @POST("groups/{groupId}/expenses")
    suspend fun createExpense(
        @Path("groupId") groupId: String,
        @Body request: CreateExpenseRequestDto
    ): ExpenseDto
}