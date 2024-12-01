package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.NewChatScreen
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatListVertical
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ConversationVertical
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.SelectedUiSubScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreenHorizontal(
    username: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatViewModel: ChatViewModel = koinViewModel()

    Row {
        val uiState by chatViewModel.uiState.collectAsState()
        val subScreen = uiState.selectedUiSubScreen

        ChatListVertical(
            isOffline = uiState.isOffline,
            selectedUsername = if (subScreen is SelectedUiSubScreen.Conversation) subScreen.selectedUsername else null,
            onLogoutClick = onLogoutClick,
            onRefreshClick = chatViewModel::refresh,
            onCreateNewChatClick = chatViewModel::setIsNewChatScreen,
            registeredUsersAndChannels = uiState.registeredUsersAndChannels,
            onChatClick = chatViewModel::setSelectedUsername,
            modifier = modifier
                .widthIn(min = 0.dp, max = 1000.dp)
                .fillMaxWidth(.35f),
            lazyListState = chatViewModel.chatListScrollState
        )

        when (subScreen) {
            is SelectedUiSubScreen.Conversation -> {
                ConversationVertical(
                    selectedUsername = subScreen.selectedUsername,
                    onBackClick = chatViewModel::unsetSubScreen,
                    onAttachImageClick = {},  // TODO - Attach images
                    onSendClick = {},
                    loggedInUsername = username,
                    chatMessagesFlow = chatViewModel.pagingDataFlow
                )
            }
            SelectedUiSubScreen.NewChat -> {
                NewChatScreen(
                    onBackClick = chatViewModel::unsetSubScreen,
                    onNewChatClick = {},
                    modifier = modifier
                )
            }
            null -> BackgroundPlaceholder()
        }
    }
}

@Composable
fun BackgroundPlaceholder() {
    Surface(
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.open_or_start_a_new_chat_to_see_messages),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
fun ChatScreenHorizontalPreview() {
    ChatScreenHorizontal(
        username = "Username",
        onLogoutClick = {}
    )
}
