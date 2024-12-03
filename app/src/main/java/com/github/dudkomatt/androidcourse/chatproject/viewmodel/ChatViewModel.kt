package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.dudkomatt.androidcourse.chatproject.data.NetworkMessagePostRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MediatorState
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessageRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageSource
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageRemoteMediator
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.toMessageEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.ChatEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.InboxEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.network.InfoApi
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SelectedUiSubScreen {
    data class Conversation(
        var selectedUsername: String,
        var pagingDataFlow: Flow<PagingData<MessageEntity>>
    ) : SelectedUiSubScreen

    data object NewChat : SelectedUiSubScreen
}

data class ChatUiState(
    var isOffline: Boolean = false,
    var fullscreenImageUrl: String? = null,
    var selectedUiSubScreen: SelectedUiSubScreen? = null,
    var inboxUsersAndRegisteredChannels: List<String>? = null,
)

class ChatViewModel(
    private val application: Application,
    private val infoApi: InfoApi,
    private val retrofitMessageApi: MessageApi,
    private val database: AppDatabase,
    private val rootViewModel: RootViewModel,
    private val networkMessagePostRepository: NetworkMessagePostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _mediatorStateFlow = MutableStateFlow<MediatorState>(MediatorState.Loading)
    val mediatorStateFlow = _mediatorStateFlow.asStateFlow()

    private val inboxDao = database.inboxDao()
    private val messageDao = database.messageDao()
    private val chatDao = database.chatDao()

    val chatListScrollState = LazyListState()

    private val pagingConfig = PagingConfig(
        pageSize = 20,
    )

    init {
        refresh()
    }

    fun setIsNewChatScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.NewChat)
    }

    fun unsetSubScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = null)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun setSelectedUsername(username: String) {
        val token = rootViewModel.uiState.value.token ?: return
        val networkMessageRepository = NetworkMessageRepository(
            retrofitMessageApi = retrofitMessageApi,
            token = token,
        )
        val messageSource = MessageSource.ChannelOrUser(username)

        _uiState.value =
            _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.Conversation(
                username,
                Pager(
                    config = pagingConfig,
                    initialKey = 0,
                    remoteMediator = MessageRemoteMediator(
                        application = application,
                        messageSource = messageSource,
                        database = database,
                        networkMessageRepository = networkMessageRepository,
                        mediatorStateFlow = _mediatorStateFlow
                    ),
                    pagingSourceFactory = {
                        database.messageDao().getBy(messageSource.channelOrUser)
                    }
                ).flow.cachedIn(viewModelScope)
            ))
    }

    fun sendMessage(text: String) {
        val fromUsername = rootViewModel.uiState.value.username ?: return
        val toUsername = when (val selectedUiSubScreen = _uiState.value.selectedUiSubScreen) {
            is SelectedUiSubScreen.Conversation -> MessageSource.ChannelOrUser(
                selectedUiSubScreen.selectedUsername
            )

            else -> return
        }.channelOrUser

        viewModelScope.launch {
            networkMessagePostRepository.postMessage(
                TextMessageRequest(
                    from = fromUsername,
                    to = toUsername,
                    data = TextMessageRequest.TextMessageInner(
                        text = TextMessageRequest.TextPayload(text),
                        image = null
                    )
                ),
            )
        }
    }

    fun showFullImage(imageUrl: String) {
        _uiState.value = _uiState.value.copy(fullscreenImageUrl = imageUrl)
    }

    fun resetFullScreenImage() {
        _uiState.value = _uiState.value.copy(fullscreenImageUrl = null)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.value =
                    _uiState.value.copy(isOffline = false, inboxUsersAndRegisteredChannels = null)

                // Get users
                val users = infoApi.getUsers()
                chatDao.insertAll(
                    users.map { ChatEntity(from = it, isChannel = false) })

                // Get channels
                val channels = infoApi.getChannels()
                chatDao.insertAll(
                    channels.map { ChatEntity(from = it, isChannel = true) })

                // Get /inbox
                val username = rootViewModel.uiState.value.username
                var inboxUsers: List<String> = listOf()
                if (username != null) {
                    val inboxMessages = getInbox(username)
                    inboxUsers = inboxMessages.flatMap { listOf(it.from, it.to) }
                        .filter { it != username }
                    inboxDao.insertAll(inboxUsers.map { InboxEntity(from = it) })
                    messageDao.insertAll(inboxMessages.map { it.toMessageEntity() })
                }

                _uiState.value =
                    _uiState.value.copy(
                        isOffline = false,
                        inboxUsersAndRegisteredChannels = (inboxUsers.toSet() - channels.toSet()).toList() + channels
                    )
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    "Registered users and channels fetch failed. Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

//                Log.d("TAG", "refreshChannelListAndRegisteredUsers: ${e.message}")

                _uiState.value = _uiState.value.copy(
                    isOffline = true,
                    inboxUsersAndRegisteredChannels = chatDao
                        .getAllChannels()
                )
            }
        }
    }

    private suspend fun getInbox(username: String): List<MessageModel> {
        var lastKnownId = 0
        val resultMessages: MutableList<MessageModel> = mutableListOf()

        val token = rootViewModel.uiState.value.token ?: return listOf()
        val networkMessageRepository = NetworkMessageRepository(
            retrofitMessageApi = retrofitMessageApi,
            token = token,
        )

        while (true) {
            val userInboxMessagesPage = networkMessageRepository.getUserInbox(
                limit = pagingConfig.pageSize,
                username = username,
                lastKnownId = lastKnownId
            )

            resultMessages += userInboxMessagesPage

            if (userInboxMessagesPage.size <= pagingConfig.pageSize) {
                break
            }

            lastKnownId = userInboxMessagesPage.maxOf { it.id }
        }

        return resultMessages
    }
}
