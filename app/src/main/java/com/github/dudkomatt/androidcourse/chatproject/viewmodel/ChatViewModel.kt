package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatUiState(
    var selectedUsername: String? = null
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun setSelectedUsername(username: String) {
        _uiState.value = _uiState.value.copy(selectedUsername = username)
    }

    fun unsetSelectedUsername() {
        _uiState.value = _uiState.value.copy(selectedUsername = null)
    }
}
