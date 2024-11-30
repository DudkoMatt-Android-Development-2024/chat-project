package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ChatTitle
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileClickableImage

@Composable
fun NewChatScreen(
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            NewChatTopBar(
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->

        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            var chatName by rememberSaveable { mutableStateOf("") }

            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .widthIn(min = 0.dp, max = 500.dp)
                    .fillMaxWidth(),
                value = chatName,
                onValueChange = { chatName = it },
                label = { Text(stringResource(R.string.channel_name_or_username)) }
            )

            Button(
                modifier = Modifier.padding(8.dp),
                content = {
                    Text("Create a new channel or text to a new person")
                },
                onClick = onNewChatClick
            )
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
                text = stringResource(R.string.new_channel_screen_title)
            )
        }
    }
}

@Composable
@Preview
fun NewChatScreenPreview() {
    NewChatScreen(
        onBackClick = {},
        onNewChatClick = {}
    )
}
