package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.NewChatScreen
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.SelectedUiSubScreen

@Composable
fun ChatScreenVertical(
    username: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatViewModel: ChatViewModel = koinViewModel()
    val uiState by chatViewModel.uiState.collectAsState()

    when (val subScreen = uiState.selectedUiSubScreen) {
        is SelectedUiSubScreen.Conversation -> {
            ConversationVertical(
                selectedUsername = subScreen.selectedUsername,
                onBackClick = chatViewModel::unsetSubScreen,
                onAttachImageClick = {},  // TODO - Attach images
                onSendClick = {},
                loggedInUsername = username,
                chatMessages = listOf()  // TODO - List messages
            )
        }
        SelectedUiSubScreen.NewChat -> {
            NewChatScreen(
                onBackClick = chatViewModel::unsetSubScreen,
                onNewChatClick = {},
                modifier = modifier
            )
        }
        null -> {
            val registeredUsersAndChannels = uiState.registeredUsers.orEmpty() + uiState.channels.orEmpty()

            ChatListVertical(
                selectedUsername = null,
                onLogoutClick = onLogoutClick,
                onRefreshClick = chatViewModel::refresh,
                onCreateNewChatClick = chatViewModel::setIsNewChatScreen,
                registeredUsersAndChannels = registeredUsersAndChannels,
                onChatClick = chatViewModel::setSelectedUsername,
                modifier = modifier
            )
        }
    }
}
