package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudkomatt.androidcourse.chatproject.data.UserSessionRepository
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
    private val userSessionRepository: UserSessionRepository
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
            userSessionRepository.removeUserInfo()
            retrieveUsernameAndToken()
        }
    }
}
