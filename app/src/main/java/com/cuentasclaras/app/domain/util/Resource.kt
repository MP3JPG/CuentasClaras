package com.cuentasclaras.app.domain.util

sealed class Resource<out T> {
    data class Success<out T>(
        val data: T,
        val warningMessage: String? = null
    ) : Resource<T>()

    data class Error(
        val message: String
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()
}