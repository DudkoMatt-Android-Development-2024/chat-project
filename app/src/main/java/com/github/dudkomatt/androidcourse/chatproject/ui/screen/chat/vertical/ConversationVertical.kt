package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
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
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.CURRENT_USERNAME
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessageWithImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getOutgoingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ReturnBackTopBarButton
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopBarText
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(chatMessages, key = { it.id }) {
                MessageEntry(
                    loggedInUsername = loggedInUsername,
                    message = it
                )
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
                TopBarText(
                    text = username
                )
            }
        }
    }
}

@Composable
fun MessageEntry(
    loggedInUsername: String,
    message: MessageModel
) {
    val isOwnMessage = message.from == loggedInUsername

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            tonalElevation = 16.dp,
            color = if (isOwnMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                if (message.data.text != null) {
                    Text(
                        text = message.data.text.text,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                if (message.data.image != null) {
                    Text("There must be an image ${message.data.image.link}")  // TODO
                }
            }
        }
    }
}

@Composable
fun ConversationBottomBar(
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        tonalElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            var prompt by rememberSaveable { mutableStateOf("") }

            AttachImageButton(
                onAttachImageClick = onAttachImageClick
            )

            TextField(
                modifier = Modifier
                    .weight(1f),
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = { Text(stringResource(R.string.message)) },
                singleLine = false,
                maxLines = 10
            )

            SendMessageBottomBarButton(
                onSendClick = onSendClick,
                enabled = prompt.isNotBlank()
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
        modifier = modifier,
        enabled = true
    )
}

@Composable
fun SendMessageBottomBarButton(
    onSendClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButtonWithCallback(
        onImageClick = onSendClick,
        imageVector = Icons.AutoMirrored.Filled.Send,
        contentDescription = stringResource(id = R.string.back_button_content_description),
        tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun IconButtonWithCallback(
    onImageClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    enabled: Boolean,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onImageClick,
        enabled = enabled
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
    val anotherUser1 = "anotherUser1"
    val anotherUser2 = "anotherUser2"

    ConversationVertical(
        onBackClick = {},
        onAttachImageClick = {},
        chatMessages = listOf(
            getIncomingMessage(id = 1, anotherUser = anotherUser1),
            getOutgoingMessage(id = 2, anotherUser = anotherUser1),
            getIncomingMessageWithImage(id = 3, anotherUser = anotherUser2),
            getOutgoingMessage(id = 4, anotherUser = anotherUser1),
            getOutgoingMessage(id = 5, anotherUser = anotherUser1),
            getOutgoingMessage(id = 6, anotherUser = anotherUser1),
            getOutgoingMessage(id = 7, anotherUser = anotherUser1),
            getOutgoingMessage(id = 8, anotherUser = anotherUser1),
            getIncomingMessage(id = 9, anotherUser = anotherUser1),
            getIncomingMessage(id = 10, anotherUser = anotherUser1),
            getOutgoingMessage(id = 11, anotherUser = anotherUser1),
            getOutgoingMessage(id = 12, anotherUser = anotherUser1),
            getIncomingMessage(id = 13, anotherUser = anotherUser1),
            getIncomingMessage(id = 14, anotherUser = anotherUser1),
            getIncomingMessage(id = 15, anotherUser = anotherUser1),
            getOutgoingMessage(id = 16, anotherUser = anotherUser1),
            getOutgoingMessage(id = 17, anotherUser = anotherUser1),
            getOutgoingMessage(id = 18, anotherUser = anotherUser1),
            getIncomingMessage(id = 19, anotherUser = anotherUser1),
            getIncomingMessage(id = 20, anotherUser = anotherUser1),
            getIncomingMessage(id = 21, anotherUser = anotherUser1),
            getOutgoingMessage(id = 22, anotherUser = anotherUser1),
        ),
        onSendClick = {},
        loggedInUsername = CURRENT_USERNAME
    )
}

@Composable
@Preview(showBackground = true)
fun MessageEntryPreview() {
    Column {
        MessageEntry(CURRENT_USERNAME, getIncomingMessage(anotherUser = "anotherUser"))
        MessageEntry(CURRENT_USERNAME, getOutgoingMessage(anotherUser = "anotherUser"))
    }
}

object PreviewData {
    const val CURRENT_USERNAME = "username"

    fun getIncomingMessage(
        anotherUser: String,
        id: Int = 1,
        currentUsername: String = CURRENT_USERNAME
    ): MessageModel {
        return MessageModel(
            id,
            anotherUser,
            currentUsername,
            MessageModel.TextMessageInner(
                text = MessageModel.TextPayload("Incoming message")
            ),
            LocalDateTime(2024, 1, 1, 1, 1)
        )
    }

    fun getOutgoingMessage(
        anotherUser: String,
        id: Int = 2,
        currentUsername: String = CURRENT_USERNAME
    ): MessageModel {
        return MessageModel(
            id,
            currentUsername,
            anotherUser,
            MessageModel.TextMessageInner(
                text = MessageModel.TextPayload("Outgoing message")
            ),
            LocalDateTime(2024, 1, 1, 1, 2)
        )
    }

    fun getIncomingMessageWithImage(
        anotherUser: String,
        id: Int = 3,
        currentUsername: String = CURRENT_USERNAME
    ): MessageModel {
        return MessageModel(
            id,
            anotherUser,
            currentUsername,
            MessageModel.TextMessageInner(
                text = MessageModel.TextPayload("Incoming message from another user with image"),
                image = MessageModel.ImagePayload("tool/tmp/scala-spiral.png")
            ),
            LocalDateTime(2024, 1, 1, 1, 2)
        )
    }
}
