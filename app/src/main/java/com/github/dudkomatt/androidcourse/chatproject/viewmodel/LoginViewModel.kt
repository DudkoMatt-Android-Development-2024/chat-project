package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onPasswordVisibilityToggle() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onSignIn(context: Context) {
        Toast.makeText(context, "Sign in clicked", Toast.LENGTH_SHORT).show()
    }

    fun onRegister(context: Context) {
        Toast.makeText(context, "Register clicked", Toast.LENGTH_SHORT).show()
    }
}
