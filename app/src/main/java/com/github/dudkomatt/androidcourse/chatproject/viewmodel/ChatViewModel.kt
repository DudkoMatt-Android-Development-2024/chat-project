package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudkomatt.androidcourse.chatproject.data.MessageRepository
import com.github.dudkomatt.androidcourse.chatproject.network.InfoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SelectedUiSubScreen {
    data class Conversation(var selectedUsername: String) : SelectedUiSubScreen
    data object NewChat : SelectedUiSubScreen
}

data class ChatUiState(
    var selectedUiSubScreen: SelectedUiSubScreen? = null,
    var registeredUsers: List<String>? = emptyList(),
    var channels: List<String>? = emptyList(),
)

class ChatViewModel(
    private val application: Application,
    private val infoApi: InfoApi,
    private val messageRepository: MessageRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun setIsNewChatScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.NewChat)
    }

    fun unsetSubScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = null)
    }

    fun setSelectedUsername(username: String) {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.Conversation(username))
    }

    fun getMessagesFromChat(username: String) {

    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.value =
                    _uiState.value.copy(registeredUsers = listOf(), channels = listOf())
                val users = infoApi.getUsers()
                val channels = infoApi.getChannels()
                _uiState.value = _uiState.value.copy(registeredUsers = users, channels = channels)
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    "Registered users and channels fetch failed. Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
