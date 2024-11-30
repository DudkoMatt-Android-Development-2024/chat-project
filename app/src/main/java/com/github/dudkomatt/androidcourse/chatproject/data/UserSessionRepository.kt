package com.github.dudkomatt.androidcourse.chatproject.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class UserSessionRepository(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    suspend fun removeUserInfo() {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(USERNAME_KEY))
            preferences.remove(stringPreferencesKey(TOKEN_KEY))
        }
    }

    suspend fun storeUsernameAndToken(username: String, token: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(USERNAME_KEY)] = username
            preferences[stringPreferencesKey(TOKEN_KEY)] = token
        }
    }

    suspend fun getToken(): String? {
        val preferences = context.dataStore.data.firstOrNull()
        return preferences?.get(stringPreferencesKey(TOKEN_KEY))
    }

    suspend fun getUsername(): String? {
        val preferences = context.dataStore.data.firstOrNull()
        return preferences?.get(stringPreferencesKey(USERNAME_KEY))
    }

    companion object {
        private const val DATA_STORE_NAME = "session_token"
        private const val TOKEN_KEY = "token"
        private const val USERNAME_KEY = "username"
    }
}
