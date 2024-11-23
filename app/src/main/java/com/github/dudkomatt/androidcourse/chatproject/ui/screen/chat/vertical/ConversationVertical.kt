package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.model.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar
import androidx.compose.foundation.lazy.items
import kotlinx.datetime.LocalDateTime


@Composable
fun ConversationVertical(
    onBackClick: () -> Unit,
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    loggedInUsername: String,
    chatMessages: List<MessageModel>,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onBackClick()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            ConversationTopBar(
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            ConversationBottomBar(
                onAttachImageClick = onAttachImageClick,
                onSendClick = onSendClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(chatMessages, key = { it.id }) {

            }
        }
    }
}

@Composable
fun ConversationTopBar(
    modifier: Modifier = Modifier,
    username: String = "Username",  // TODO - Remove default
    onBackClick: () -> Unit
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBackClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThumbProfileImage()
                UsernameText(
                    username = username
                )
            }
        }
    }
}

@Composable
fun ReturnBackTopBarButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onBackClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(id = R.string.back_button_content_description),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun UsernameText(
    username: String
) {
    Text(
        modifier = Modifier.padding(12.dp),
        text = username,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun ConversationBottomBar(
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomHeightBar = 56.dp
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(bottomHeightBar),
        tonalElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var prompt by rememberSaveable { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .weight(1f),
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = { Text(stringResource(R.string.message)) },
                singleLine = false
            )
            AttachImageButton(
                onAttachImageClick = onAttachImageClick
            )
            SendMessageBottomBarButton(
                onSendClick = onSendClick
            )
        }
    }
}

@Composable
fun AttachImageButton(
    onAttachImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButtonWithCallback(
        onImageClick = onAttachImageClick,
        imageVector = Icons.Default.AttachFile,
        contentDescription = stringResource(id = R.string.attach_iamge_button_content_description),
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun SendMessageBottomBarButton(
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButtonWithCallback(
        onImageClick = onSendClick,
        imageVector = Icons.AutoMirrored.Filled.Send,
        contentDescription = stringResource(id = R.string.back_button_content_description),
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun IconButtonWithCallback(
    onImageClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onImageClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Composable
@Preview
fun ConversationVerticalPreview() {
    val currentUsername = "username"
    val anotherUser1 = "anotherUser1"
    val anotherUser2 = "anotherUser2"

    ConversationVertical(
        onBackClick = {},
        onAttachImageClick = {},
        chatMessages = listOf(
            MessageModel(
                1,
                anotherUser1,
                currentUsername,
                MessageModel.TextMessageInner(
                    text = MessageModel.TextPayload("Incoming message")
                ),
                LocalDateTime(2024, 1, 1, 1, 1)
            ),
            MessageModel(
                2,
                currentUsername,
                anotherUser1,
                MessageModel.TextMessageInner(
                    text = MessageModel.TextPayload("Outgoing message")
                ),
                LocalDateTime(2024, 1, 1, 1, 2)
            ),
            MessageModel(
                3,
                anotherUser2,
                currentUsername,
                MessageModel.TextMessageInner(
                    text = MessageModel.TextPayload("Incoming message from another user with image"),
                    image = MessageModel.ImagePayload("tool/tmp/scala-spiral.png")
                ),
                LocalDateTime(2024, 1, 1, 1, 2)
            ),
        ),
        onSendClick = {},
        loggedInUsername = currentUsername
    )
}
