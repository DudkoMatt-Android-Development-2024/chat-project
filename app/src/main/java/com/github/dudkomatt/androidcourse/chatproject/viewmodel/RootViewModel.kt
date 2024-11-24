package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudkomatt.androidcourse.chatproject.data.SessionTokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RootUIState(
    var isLoggedIn: Boolean? = null,
)

class RootViewModel(
    private val sessionTokenManager: SessionTokenManager
): ViewModel() {
    private val _uiState = MutableStateFlow(RootUIState())
    val uiState: StateFlow<RootUIState> = _uiState.asStateFlow()

    init {
        isLoggedIn()
    }

    private fun isLoggedIn() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isLoggedIn = sessionTokenManager.getToken() != null)
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean = true) {
        _uiState.value = _uiState.value.copy(isLoggedIn = isLoggedIn)
    }

    fun logOut() {
        viewModelScope.launch {
            sessionTokenManager.removeToken()
            setLoggedIn(false)
        }
    }
}
