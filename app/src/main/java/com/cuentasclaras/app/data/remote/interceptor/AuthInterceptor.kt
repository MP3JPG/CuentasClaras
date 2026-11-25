package com.cuentasclaras.app.data.remote.interceptor

import com.cuentasclaras.app.data.local.datastore.SessionPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionPreferences: SessionPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // No enviamos token en el endpoint de inicio de sesión
        if (originalRequest.url.encodedPath.endsWith("auth/login")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            sessionPreferences.getToken()
        }

        val authenticatedRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}