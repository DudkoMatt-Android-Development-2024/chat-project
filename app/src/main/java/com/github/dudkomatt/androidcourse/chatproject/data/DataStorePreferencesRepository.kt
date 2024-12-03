package com.github.dudkomatt.androidcourse.chatproject.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

sealed interface ThemeState {
    data object SystemDefault : ThemeState
    data object Dark : ThemeState
    data object Light : ThemeState
}

class DataStorePreferencesRepository(
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

    suspend fun storeTheme(theme: ThemeState) {
        context.dataStore.edit { preferences ->
            when (theme) {
                ThemeState.Dark -> preferences[stringPreferencesKey(THEME_KEY)] = "Dark"
                ThemeState.Light -> preferences[stringPreferencesKey(THEME_KEY)] = "Light"
                ThemeState.SystemDefault -> preferences.remove(stringPreferencesKey(THEME_KEY))
            }
        }
    }

    suspend fun getTheme(): ThemeState {
        val preferences = context.dataStore.data.firstOrNull()
        return when (preferences?.get(stringPreferencesKey(THEME_KEY))) {
            "Dark" -> ThemeState.Dark
            "Light" -> ThemeState.Light
            else -> ThemeState.SystemDefault
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "session_token"
        private const val TOKEN_KEY = "token"
        private const val USERNAME_KEY = "username"
        private const val THEME_KEY = "theme"
    }
}
