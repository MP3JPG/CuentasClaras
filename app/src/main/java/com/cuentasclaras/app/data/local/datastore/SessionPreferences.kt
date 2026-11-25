package com.cuentasclaras.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cuentasclaras.app.domain.model.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "cuentas_claras_session"
)

@Singleton
class SessionPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    val userSessionFlow: Flow<UserSession?> = context.dataStore.data.map { preferences ->
        val token = preferences[KEY_TOKEN]
        val userId = preferences[KEY_USER_ID]
        val userName = preferences[KEY_USER_NAME]
        val userEmail = preferences[KEY_USER_EMAIL]

        if (!token.isNullOrBlank() && !userId.isNullOrBlank()) {
            UserSession(
                token = token,
                userId = userId,
                userName = userName ?: "Usuario",
                userEmail = userEmail ?: ""
            )
        } else {
            null
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }.first()
    }

    suspend fun saveSession(session: UserSession) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = session.token
            preferences[KEY_USER_ID] = session.userId
            preferences[KEY_USER_NAME] = session.userName
            preferences[KEY_USER_EMAIL] = session.userEmail
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}