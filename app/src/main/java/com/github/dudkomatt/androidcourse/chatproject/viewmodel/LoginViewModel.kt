package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudkomatt.androidcourse.chatproject.model.request.LoginRequest
import com.github.dudkomatt.androidcourse.chatproject.network.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    var username: String = "",
    var password: String = "",
    var isPasswordVisible: Boolean = false,
)

class LoginViewModel(
    private val authApi: AuthApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onPasswordVisibilityToggle() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun onSignIn(context: Context) {
        viewModelScope.launch {
            try {
                val responseWithToken = authApi.loginPost(
                    LoginRequest(
                        _uiState.value.username,
                        _uiState.value.password
                    )
                )
                Toast.makeText(
                    context,
                    "Sign in SUCCESS. Response: $responseWithToken",
                    Toast.LENGTH_LONG
                )
                    .show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Sign in failed. Please try again. Error: ${e.message}",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    fun onRegister(context: Context) {
        Toast.makeText(context, "Register clicked", Toast.LENGTH_SHORT).show()
    }
}
