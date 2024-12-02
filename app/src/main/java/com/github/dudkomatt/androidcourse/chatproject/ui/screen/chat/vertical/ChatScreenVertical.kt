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
                onChatRefreshClick = chatViewModel::refreshConversation,
                onBackClick = chatViewModel::unsetSubScreen,
                onAttachImageClick = {},  // TODO - Attach images
                onSendClick = {},
                loggedInUsername = username,
                chatMessagesFlow = chatViewModel.pagingDataFlow,
                onImageClick = chatViewModel::showFullImage
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
            ChatListVertical(
                isOffline = uiState.isOffline,
                selectedUsername = null,
                onLogoutClick = onLogoutClick,
                onRefreshClick = chatViewModel::refreshChatList,
                onCreateNewChatClick = chatViewModel::setIsNewChatScreen,
                registeredUsersAndChannels = uiState.registeredUsersAndChannels,
                onChatClick = chatViewModel::setSelectedUsername,
                modifier = modifier,
                lazyListState = chatViewModel.chatListScrollState
            )
        }
    }
}
