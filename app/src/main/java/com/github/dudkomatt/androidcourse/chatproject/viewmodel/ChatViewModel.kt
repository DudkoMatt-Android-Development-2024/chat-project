package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.data.DataStorePreferencesRepository
import com.github.dudkomatt.androidcourse.chatproject.data.NetworkMessagePostRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MediatorState
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageRemoteMediator
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageSource
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessageRepository
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

sealed interface AttachImageState {
    data object Nothing : AttachImageState
    data class Image(val uri: Uri) : AttachImageState
}

data class InboxOrChannelEntry(
    val from: String,
    val isInbox: Boolean
)

data class ChatUiState(
    var isOffline: Boolean = false,
    var fullscreenImageUrl: String? = null,
    var selectedUiSubScreen: SelectedUiSubScreen? = null,
    var inboxUsersAndRegisteredChannels: List<InboxOrChannelEntry>? = null,
)

class ChatViewModel(
    private val application: Application,
    private val infoApi: InfoApi,
    private val retrofitMessageApi: MessageApi,
    private val database: AppDatabase,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository,
    private val networkMessagePostRepository: NetworkMessagePostRepository
) : ViewModel() {

    companion object {
        const val CHANNEL_POSTFIX = "@channel"
    }

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _mediatorStateFlow = MutableStateFlow<MediatorState>(MediatorState.Loading)
    val mediatorStateFlow = _mediatorStateFlow.asStateFlow()

    private val _selectedImageStateFlow =
        MutableStateFlow<AttachImageState>(AttachImageState.Nothing)
    val selectedImageStateFlow = _selectedImageStateFlow.asStateFlow()

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

    fun setSelectedChatEntry(channelOrUsername: String, isChannel: Boolean) {
        setSelectedChatEntry(
            InboxOrChannelEntry(
                from = if (isChannel) channelOrUsername + CHANNEL_POSTFIX else channelOrUsername,
                isInbox = !isChannel
            )
        )
    }

    fun logOut() {
        _uiState.value = ChatUiState()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun setSelectedChatEntry(selectedEntry: InboxOrChannelEntry) {
        viewModelScope.launch {
            val token = dataStorePreferencesRepository.getToken()
            val username = dataStorePreferencesRepository.getUsername()


            if (token == null) return@launch
            if (username == null) return@launch

            val networkMessageRepository = NetworkMessageRepository(
                retrofitMessageApi = retrofitMessageApi,
                token = token,
            )

            val messageSource = if (selectedEntry.isInbox) MessageSource.Inbox(
                myUsername = username,
                anotherUsername = selectedEntry.from
            ) else MessageSource.ChannelOrUser(selectedEntry.from)

            _uiState.value =
                _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.Conversation(
                    selectedUsername = selectedEntry.from,
                    pagingDataFlow = Pager(
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
                            if (selectedEntry.isInbox) {
                                database.messageDao().getByInbox(
                                    myUsername = username,
                                    anotherUsername = selectedEntry.from
                                )
                            } else {
                                database.messageDao().getBy(channelOrUsername = selectedEntry.from)
                            }
                        }
                    ).flow.cachedIn(viewModelScope)
                ))
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val fromUsername = dataStorePreferencesRepository.getUsername() ?: return@launch
            val toUsername = when (val selectedUiSubScreen = _uiState.value.selectedUiSubScreen) {
                is SelectedUiSubScreen.Conversation -> MessageSource.ChannelOrUser(
                    selectedUiSubScreen.selectedUsername
                )

                else -> return@launch
            }.channelOrUser

            val imageUri: Uri? = when (val imageSelectionState = _selectedImageStateFlow.value) {
                is AttachImageState.Image -> imageSelectionState.uri
                AttachImageState.Nothing -> null
            }

            if (imageUri == null) {
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
            } else {
                Toast.makeText(
                    application.applicationContext,
                    application.applicationContext.getString(R.string.not_implemented_due_to_411_length_required_error),
                    Toast.LENGTH_SHORT
                ).show()

//                application.applicationContext.contentResolver.openInputStream(imageUri).use { inputStream ->
//                    if (inputStream == null) return@launch
//
//                    val byteArrayOutputStream = ByteArrayOutputStream()
//                    val bitmap = MediaStore.Images.Media.getBitmap(application.applicationContext.contentResolver, imageUri)
//
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//                    val byteArray = byteArrayOutputStream.toByteArray()
//
//                    networkMessagePostRepository.postMessage(
//                        textMessage = TextMessageRequest(
//                            from = fromUsername,
//                            to = toUsername,
//                            data = null
//                        ),
//                        image = MultipartBody.Part.createFormData(
//                            "picture",
//                            null,
//                            byteArray.toRequestBody(contentType = "image/png".toMediaType())
//                        )
//                    )
//                }
            }
        }

    }

    fun setSelectedImage(uri: Uri) {
        _selectedImageStateFlow.value = AttachImageState.Image(uri)
    }

    fun unsetSelectedImage() {
        _selectedImageStateFlow.value = AttachImageState.Nothing
    }

    fun showFullImage(imageUrl: String) {
        _uiState.value = _uiState.value.copy(fullscreenImageUrl = imageUrl)
    }

    fun resetFullScreenImage() {
        _uiState.value = _uiState.value.copy(fullscreenImageUrl = null)
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                if (dataStorePreferencesRepository.getUsername() == null || dataStorePreferencesRepository.getToken() == null) {
                    return@launch
                }

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
                val username = dataStorePreferencesRepository.getUsername()
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
                        inboxUsersAndRegisteredChannels = (inboxUsers.toSet() - channels.toSet()).map {
                            InboxOrChannelEntry(from = it, isInbox = true)
                        }.toList()
                                + channels.map { InboxOrChannelEntry(from = it, isInbox = false) }
                            .toList()
                    )
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    application.applicationContext.getString(
                        R.string.registered_users_and_channels_fetch_failed_error,
                        e.message
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                _uiState.value = _uiState.value.copy(
                    isOffline = true,
                    inboxUsersAndRegisteredChannels = inboxDao.getAll().map {
                        InboxOrChannelEntry(from = it, isInbox = true)
                    }.toList() + chatDao
                        .getAllChannels()
                        .map { InboxOrChannelEntry(from = it, isInbox = false) }
                        .toList()
                )
            }
        }
    }

    private suspend fun getInbox(username: String): List<MessageModel> {
        var lastKnownId = 0
        val resultMessages: MutableList<MessageModel> = mutableListOf()

        val token = dataStorePreferencesRepository.getToken() ?: return listOf()
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
