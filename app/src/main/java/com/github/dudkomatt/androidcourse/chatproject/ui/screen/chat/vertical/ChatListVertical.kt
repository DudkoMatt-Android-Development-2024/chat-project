package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.model.ChatEntryModel
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ChatListEntry
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileClickableImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar

@Composable
fun ChatListVertical(
    createNewChatOrChannelFunction: () -> Unit,
    chatEntries: List<ChatEntryModel> = emptyList(),  // TODO - Remove default
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = { ChatListTopBar() },
        floatingActionButton = { BottomFloatingActionButton(createNewChatOrChannelFunction) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // TODO
            items(chatEntries, key = { it.from }) {
                ChatListEntry(
                    from = it.from,
                    lastMessage = it.lastMessage,
                )
            }
        }
    }
}

@Composable
fun ChatListTopBar(
    onProfileImageClick: () -> Unit = {}  // TODO - Remove default
) {
    TopAppBar {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = stringResource(R.string.chats_screen_title),
                style = MaterialTheme.typography.titleLarge
            )
            ThumbProfileClickableImage(
                modifier = Modifier.padding(all = 2.dp),
                onImageClick = onProfileImageClick
            )
        }
    }
}

@Composable
fun BottomFloatingActionButton(
    createNewChatOrChannelFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = createNewChatOrChannelFunction
    ) {
        Icon(
            Icons.Default.Textsms,
            contentDescription = stringResource(R.string.create_new_chat_or_channel_floating_action_button_description)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChatListVerticalPreview() {
    val loremIpsum = LoremIpsum(30)

    ChatScreenVertical(
        createNewChatOrChannelFunction = {},
        chatEntries = (1..20).map {
            ChatEntryModel("From $it", loremIpsum.values.joinToString())
        }
    )
}
