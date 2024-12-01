package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.model.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.CURRENT_USERNAME
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessageWithImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getOutgoingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.IconButtonWithCallback
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ReturnBackTopBarButton
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopBarText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


@Composable
fun ConversationVertical(
    selectedUsername: String,
    onBackClick: () -> Unit,
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    loggedInUsername: String,
    chatMessagesFlow: Flow<PagingData<MessageModel>>,
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
                username = selectedUsername,
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
        val lazyPagingItems = chatMessagesFlow.collectAsLazyPagingItems()

        Box(modifier = Modifier.padding(innerPadding)) {

            val state = lazyPagingItems.loadState.refresh
            when (state) {
                is LoadState.Loading -> {
                    Text("Loading")  // TODO
                }

                is LoadState.Error -> {
                    Log.d("TAG", "ConversationVertical: ${state.error.message}")  // TODO
                    Log.d("TAG", "ConversationVertical: ${state.error.stackTraceToString()}")  // TODO
                    Text("Error ${state.error}")  // TODO
                }

                is LoadState.NotLoading -> {
//                    Text("Loaded")  // TODO

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        items(count = lazyPagingItems.itemCount) { index ->
                            val item = lazyPagingItems[index]
                            if (item != null) {
                                MessageEntry(
                                    loggedInUsername = loggedInUsername,
                                    message = item
                                )
                            } else {
                                Text("That's it")  // TODO
                            }
                        }
                    }
                }

                else -> Text("Something else")  // TODO
            }
        }

    }
}

@Composable
fun ConversationTopBar(
    username: String,
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
            Row(
                modifier = Modifier
                    .fillMaxSize(),
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
            val padding = 4.dp

            AttachImageButton(
                modifier = Modifier.padding(padding),
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
                modifier = Modifier.padding(padding),
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
@Preview
fun ConversationVerticalPreview() {
    val anotherUser1 = "anotherUser1"
    val anotherUser2 = "anotherUser2"

    val messages = listOf(
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
    )

    ConversationVertical(
        onBackClick = {},
        onAttachImageClick = {},
        chatMessagesFlow = MutableStateFlow(PagingData.from(messages)),
        onSendClick = {},
        loggedInUsername = CURRENT_USERNAME,
        selectedUsername = "Selected username"
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
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
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
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
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
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
        )
    }
}
