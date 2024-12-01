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
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessagePagingRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageSource
import com.github.dudkomatt.androidcourse.chatproject.data.UserSessionRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessagePagingRoomRepository
import com.github.dudkomatt.androidcourse.chatproject.data.paging.MessageRemoteMediator
import com.github.dudkomatt.androidcourse.chatproject.model.room.ChatEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.network.InfoApi
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

sealed interface SelectedUiSubScreen {
    data class Conversation(var selectedUsername: String) : SelectedUiSubScreen
    data object NewChat : SelectedUiSubScreen
}

data class ChatUiState(
    var isOffline: Boolean = false,
    var selectedUiSubScreen: SelectedUiSubScreen? = null,
    var registeredUsersAndChannels: List<String>? = emptyList(),
)

class ChatViewModel(
    private val application: Application,
    private val infoApi: InfoApi,
    private val userSessionRepository: UserSessionRepository,
    private val retrofitMessageApi: MessageApi,
    private val database: AppDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val chatDao = database.chatDao()

    val chatListScrollState = LazyListState()

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagingDataFlow: Flow<PagingData<MessageEntity>> = uiState.flatMapLatest { state ->
        val messageSource = when (val selectedUiSubScreen = state.selectedUiSubScreen) {
            is SelectedUiSubScreen.Conversation -> MessageSource.ChannelOrUser(
                selectedUiSubScreen.selectedUsername
            )

            else -> return@flatMapLatest flowOf()
        }

        val networkMessagePagingRepository = NetworkMessagePagingRepository(
            messageSource = messageSource,
            retrofitMessageApi = retrofitMessageApi,
            userSessionRepository = userSessionRepository
        )

        Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0,
            remoteMediator = MessageRemoteMediator(
                messageSource = messageSource,
                database = database,
                networkMessagePagingRepository = networkMessagePagingRepository
            ),
            pagingSourceFactory = {
                MessagePagingRoomRepository(
                    messageSource = messageSource,
                    messageDao = database.messageDao()
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    init {
        refresh()
    }

    fun setIsNewChatScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.NewChat)
    }

    fun unsetSubScreen() {
        _uiState.value = _uiState.value.copy(selectedUiSubScreen = null)
    }

    fun setSelectedUsername(username: String) {
        _uiState.value =
            _uiState.value.copy(selectedUiSubScreen = SelectedUiSubScreen.Conversation(username))
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.value =
                    _uiState.value.copy(isOffline = false, registeredUsersAndChannels = null)
                val registeredUsersAndChannels = infoApi.getUsers() + infoApi.getChannels()
                chatDao.insertAll(registeredUsersAndChannels.map { ChatEntity(it) })
                _uiState.value =
                    _uiState.value.copy(isOffline = false, registeredUsersAndChannels = registeredUsersAndChannels)
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    "Registered users and channels fetch failed. Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

                _uiState.value = _uiState.value.copy(
                    isOffline = true,
                    registeredUsersAndChannels = chatDao
                        .getAll()
                        .map {
                            it.from
                        }
                )
            }
        }
    }
}
