package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ModeEditOutline
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
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.IconButtonWithCallback
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileClickableImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar

@Composable
fun ChatListVertical(
    onLogoutClick: () -> Unit,
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
    onChatClick: (String) -> Unit,
    chatEntries: List<ChatEntryModel> = emptyList(),  // TODO - Remove default
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = { ChatListTopBar(
            onLogoutClick = onLogoutClick
        ) },
        floatingActionButton = { BottomFloatingActionButton(
            createNewChat = createNewChat,
            createNewChannel = createNewChannel
        ) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(chatEntries, key = { it.from }) {
                ChatListEntry(
                    from = it.from,
                    lastMessage = it.lastMessage,
                    onChatClick = onChatClick
                )
            }
        }
    }
}

@Composable
fun ChatListTopBar(
    onLogoutClick: () -> Unit,
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
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButtonWithCallback(
                    onImageClick = onLogoutClick,
                    imageVector = Icons.Default.Cancel,
                    contentDescription = stringResource(R.string.log_out_button),
                    tint = MaterialTheme.colorScheme.onSurface,
                    enabled = true
                )
                ThumbProfileClickableImage(
                    modifier = Modifier.padding(all = 2.dp),
                    onImageClick = onProfileImageClick
                )
            }
        }
    }
}

@Composable
fun BottomFloatingActionButton(
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        FloatingActionButton(
            modifier = modifier,
            onClick = createNewChannel
        ) {
            Icon(
                Icons.Default.Campaign,
                contentDescription = stringResource(R.string.create_new_channel_floating_action_button_description)
            )
        }

        FloatingActionButton(
            modifier = modifier,
            onClick = createNewChat
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(R.string.create_new_chat_floating_action_button_description)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatListVerticalPreview() {
    val loremIpsum = LoremIpsum(30)

    ChatListVertical(
        onLogoutClick = {},
        createNewChannel = {},
        createNewChat = {},
        onChatClick = {},
        chatEntries = (1..20).map {
            ChatEntryModel("From $it", loremIpsum.values.joinToString())
        }
    )
}
