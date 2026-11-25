package com.cuentasclaras.app.data.remote.dto

import com.cuentasclaras.app.domain.model.UserSession

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val token: String,
    val user: UserDto
) {
    fun toDomain(): UserSession {
        return UserSession(
            token = token,
            userId = user.id,
            userName = user.name,
            userEmail = user.email
        )
    }
}

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)