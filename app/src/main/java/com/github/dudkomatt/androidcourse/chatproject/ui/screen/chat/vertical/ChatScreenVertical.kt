package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.github.dudkomatt.androidcourse.chatproject.model.ChatEntryModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.RootViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreenVertical(
    onLogoutClick: () -> Unit,
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
    chatEntries: List<ChatEntryModel> = emptyList(),  // TODO - Remove default
    modifier: Modifier = Modifier
) {
    val rootViewModel: RootViewModel = koinViewModel()
    val rootUiState by rootViewModel.uiState.collectAsState()

    val username = rootUiState.username
    if (username != null) {
        val chatViewModel: ChatViewModel = koinViewModel()
        val uiState by chatViewModel.uiState.collectAsState()

        if (uiState.selectedUsername != null) {
            ConversationVertical(
                onBackClick = chatViewModel::unsetSelectedUsername,
                onAttachImageClick = {},  // TODO - Attach images
                onSendClick = {},
                loggedInUsername = username,
                chatMessages = listOf()  // TODO - List messages
            )
        } else {
            ChatListVertical(
                onLogoutClick = onLogoutClick,
                createNewChannel = createNewChannel,
                createNewChat = createNewChat,
                chatEntries = chatEntries,
                onChatClick = chatViewModel::setSelectedUsername,
                modifier = modifier
            )
        }
    }
}

@Composable
@Preview
fun ChatScreenVerticalPreview() {
    val loremIpsum = LoremIpsum(30)

    ChatScreenVertical(
        onLogoutClick = {},
        createNewChannel = {},
        createNewChat = {},
        chatEntries = (1..20).map {
            ChatEntryModel("From $it", loremIpsum.values.joinToString())
        }
    )
}
