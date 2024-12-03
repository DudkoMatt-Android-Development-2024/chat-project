package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.NewChatScreen
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.SelectedUiSubScreen
import org.koin.androidx.compose.koinViewModel

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
                onSendClick = chatViewModel::sendMessage,
                loggedInUsername = username,
                chatMessagesFlow = subScreen.pagingDataFlow,
                onImageClick = chatViewModel::showFullImage
            )
        }
        SelectedUiSubScreen.NewChat -> {
            NewChatScreen(
                onBackClick = chatViewModel::unsetSubScreen,
                onNewChatClick = chatViewModel::setSelectedChatEntry,
                modifier = modifier
            )
        }
        null -> {
            ChatListVertical(
                isOffline = uiState.isOffline,
                selectedUsername = null,
                onLogoutClick = onLogoutClick,
                onRefreshClick = chatViewModel::refresh,
                onCreateNewChatClick = chatViewModel::setIsNewChatScreen,
                inboxUsersAndChannels = uiState.inboxUsersAndRegisteredChannels,
                onChatClick = chatViewModel::setSelectedChatEntry,
                modifier = modifier,
                lazyListState = chatViewModel.chatListScrollState
            )
        }
    }
}
