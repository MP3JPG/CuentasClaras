package com.cuentasclaras.app.domain.model

data class UserSession(
    val token: String,
    val userId: String,
    val userName: String,
    val userEmail: String
)