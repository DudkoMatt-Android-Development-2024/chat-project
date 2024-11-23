package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ReturnBackTopBarButton
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopBarText
import androidx.compose.foundation.lazy.items
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ChatTitle
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileClickableImage

@Composable
fun NewChatScreen(
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    registeredUsersAndChannels: List<String>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            NewChatTopBar(
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(registeredUsersAndChannels, key = { it }) {
                UserEntry(
                    modifier = Modifier.clickable { onUserClick(it) },
                    username = it
                )
            }
        }
    }
}

@Composable
fun UserEntry(
    username: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ThumbProfileClickableImage(
            modifier = Modifier.padding(all = 2.dp),
            onImageClick = {}
        )
        ChatTitle(
            from = username
        )
    }
}

@Composable
fun NewChatTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar { barHeight ->
        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            ReturnBackTopBarButton(
                modifier = Modifier
                    .size(barHeight),
                onBackClick = onBackClick
            )
            TopBarText(
                text = stringResource(R.string.new_chat_screen_title)
            )
        }
    }
}

@Composable
@Preview
fun NewChatScreenPreview() {
    NewChatScreen(
        onBackClick = {},
        onUserClick = {},
        registeredUsersAndChannels = listOf("user1", "user2", "user3")
    )
}
