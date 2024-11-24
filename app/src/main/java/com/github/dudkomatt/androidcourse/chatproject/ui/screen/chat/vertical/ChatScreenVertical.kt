package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.NewChatScreen

@Composable
fun ChatScreenVertical(
    username: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatViewModel: ChatViewModel = koinViewModel()
    val uiState by chatViewModel.uiState.collectAsState()

    val selectedUsername = uiState.selectedUsername
    if (selectedUsername != null) {
        ConversationVertical(
            selectedUsername = selectedUsername,
            onBackClick = chatViewModel::unsetSelectedUsername,
            onAttachImageClick = {},  // TODO - Attach images
            onSendClick = {},
            loggedInUsername = username,
            chatMessages = listOf()  // TODO - List messages
        )
    } else if (uiState.isNewChatScreen) {
        NewChatScreen(
            onBackClick = chatViewModel::unsetIsNewChatScreen,
            onNewChatClick = {},
            modifier = modifier
        )
    } else {
        val registeredUsersAndChannels = uiState.registeredUsers.orEmpty() + uiState.channels.orEmpty()

        ChatListVertical(
            onLogoutClick = onLogoutClick,
            onRefreshClick = chatViewModel::refresh,
            onCreateNewChatClick = chatViewModel::setIsNewChatScreen,
            registeredUsersAndChannels = registeredUsersAndChannels,
            onChatClick = chatViewModel::setSelectedUsername,
            modifier = modifier
        )

    }
}
