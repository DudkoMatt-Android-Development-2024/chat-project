package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.github.dudkomatt.androidcourse.chatproject.model.ChatEntryModel

@Composable
fun ChatScreenVertical(
    onLogoutClick: () -> Unit,
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
    chatEntries: List<ChatEntryModel> = emptyList(),  // TODO - Remove default
    modifier: Modifier = Modifier
) {
    ChatListVertical(
        onLogoutClick = onLogoutClick,
        createNewChannel = createNewChannel,
        createNewChat = createNewChat,
        chatEntries = chatEntries,
        modifier = modifier
    )

    // TODO
    // if selected
//    ConversationVertical()
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
