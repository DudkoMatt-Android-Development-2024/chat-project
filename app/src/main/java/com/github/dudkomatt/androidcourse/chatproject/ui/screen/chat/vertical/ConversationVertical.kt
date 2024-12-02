package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ThumbProfileImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopAppBar
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.CURRENT_USERNAME
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getIncomingMessageWithImage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.PreviewData.getOutgoingMessage
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.AsyncImageComponent
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.IconButtonWithCallback
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.OfflineIcon
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.ReturnBackTopBarButton
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.TopBarText
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.loading.LoadingScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


@Composable
fun ConversationVertical(
    selectedUsername: String,
    onChatRefreshClick: () -> Unit,
    onBackClick: () -> Unit,
    onAttachImageClick: () -> Unit,
    onSendClick: () -> Unit,
    onImageClick: (String) -> Unit,
    loggedInUsername: String,
    chatMessagesFlow: Flow<PagingData<MessageEntity>>,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onBackClick()
    }

    val lazyPagingItems = chatMessagesFlow.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            ConversationTopBar(
                username = selectedUsername,
                onChatRefreshClick = onChatRefreshClick,
                onBackClick = onBackClick,
                isOffline = lazyPagingItems.loadState.refresh is LoadState.Error
            )
        },
        bottomBar = {
            ConversationBottomBar(
                onAttachImageClick = onAttachImageClick,
                onSendClick = onSendClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (val state = lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingScreen()
                }

                is LoadState.Error -> {
                    Log.d("TAG", "ConversationVertical: ${state.error.message}")  // TODO
                    Log.d(
                        "TAG",
                        "ConversationVertical: ${state.error.stackTraceToString()}"
                    )  // TODO
                    Text("Error ${state.error}")  // TODO

                    MessageLazyColumn(
                        innerPadding = innerPadding,
                        lazyPagingItems = lazyPagingItems,
                        loggedInUsername = loggedInUsername,
                        onImageClick = onImageClick
                    )
                }

                is LoadState.NotLoading -> {
                    MessageLazyColumn(
                        innerPadding = innerPadding,
                        lazyPagingItems = lazyPagingItems,
                        loggedInUsername = loggedInUsername,
                        onImageClick = onImageClick
                    )
                }

                else -> Text("Something else")  // TODO
            }
        }

    }
}

@Composable
private fun MessageLazyColumn(
    innerPadding: PaddingValues,
    lazyPagingItems: LazyPagingItems<MessageEntity>,
    loggedInUsername: String,
    onImageClick: (String) -> Unit
) {
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
                    message = item,
                    onImageClick = onImageClick
                )
            }
        }
    }
}

@Composable
fun ConversationTopBar(
    username: String,
    onChatRefreshClick: () -> Unit,
    onBackClick: () -> Unit,
    isOffline: Boolean,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isOffline) {
                        OfflineIcon(
                            modifier = Modifier.padding(end = barHeight / 4)
                        )
                    }
                    RefreshButton(onRefreshClick = onChatRefreshClick)
                }
            }
        }
    }
}

@Composable
fun RefreshButton(modifier: Modifier = Modifier, onRefreshClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onRefreshClick
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(R.string.refresh_button),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MessageEntry(
    loggedInUsername: String,
    onImageClick: (String) -> Unit,
    message: MessageEntity
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
                if (message.text != null) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                if (message.imageUrl != null) {
                    AsyncImageComponent(
                        modifier = Modifier.clickable {
                            onImageClick(message.imageUrl)
                        },
                        imageUrl = message.imageUrl,
                        isThumb = true,
                    )
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
        onChatRefreshClick = {},
        onBackClick = {},
        onAttachImageClick = {},
        chatMessagesFlow = MutableStateFlow(PagingData.from(messages)),
        onSendClick = {},
        loggedInUsername = CURRENT_USERNAME,
        selectedUsername = "Selected username",
        onImageClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun MessageEntryPreview() {
    Column {
        MessageEntry(loggedInUsername = CURRENT_USERNAME, onImageClick = {}, message = getIncomingMessage(anotherUser = "anotherUser"))
        MessageEntry(loggedInUsername = CURRENT_USERNAME, onImageClick = {}, message = getOutgoingMessage(anotherUser = "anotherUser"))
    }
}

object PreviewData {
    const val CURRENT_USERNAME = "username"

    fun getIncomingMessage(
        anotherUser: String,
        id: Int = 1,
        currentUsername: String = CURRENT_USERNAME
    ): MessageEntity {
        return MessageEntity(
            id,
            anotherUser,
            currentUsername,
            "Incoming message",
            null,
            LocalDateTime(2024, 1, 1, 1, 1)
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
        )
    }

    fun getOutgoingMessage(
        anotherUser: String,
        id: Int = 2,
        currentUsername: String = CURRENT_USERNAME
    ): MessageEntity {
        return MessageEntity(
            id,
            currentUsername,
            anotherUser,
            "Outgoing message",
            null,
            LocalDateTime(2024, 1, 1, 1, 2)
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
        )
    }

    fun getIncomingMessageWithImage(
        anotherUser: String,
        id: Int = 3,
        currentUsername: String = CURRENT_USERNAME
    ): MessageEntity {
        return MessageEntity(
            id,
            anotherUser,
            currentUsername,
            "Incoming message from another user with image",
            "tool/tmp/scala-spiral.png",
            LocalDateTime(2024, 1, 1, 1, 2)
                .toInstant(TimeZone.of("Europe/Moscow"))
                .epochSeconds
        )
    }
}
