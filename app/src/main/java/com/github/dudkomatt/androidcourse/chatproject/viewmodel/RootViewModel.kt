package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.github.dudkomatt.androidcourse.chatproject.data.LogConfig
import com.github.dudkomatt.androidcourse.chatproject.data.UserSessionRepository
import com.github.dudkomatt.androidcourse.chatproject.network.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RootUIState(
    var isLoading: Boolean = true,
    var username: String? = null,
    var token: String? = null,
) {
    fun isLoggedIn(): Boolean {
        return username != null && token != null
    }
}

class RootViewModel(
    private val userSessionRepository: UserSessionRepository,
    private val authApi: AuthApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(RootUIState())
    val uiState: StateFlow<RootUIState> = _uiState.asStateFlow()

    init {
        retrieveUsernameAndToken()
    }

    fun retrieveUsernameAndToken() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    username = userSessionRepository.getUsername(),
                    token = userSessionRepository.getToken(),
                    isLoading = false
                )
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.value.token?.let {
                try {
                    authApi.logoutPost(it)
                } catch (e: Exception) {
                    // Ignore any errors
                    Log.e(
                        LogConfig.ERROR_LOGOUT_TAG,
                        "logOut: Exception occurred. Resetting UI state",
                        e
                    )
                }
            }

            userSessionRepository.removeUserInfo()
            retrieveUsernameAndToken()
        }
    }
}
