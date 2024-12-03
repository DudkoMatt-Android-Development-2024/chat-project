package com.github.dudkomatt.androidcourse.chatproject.data.paging

import android.app.Application
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessageRepository.Companion.isEndOfPaginationReached
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.toMessageEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow

sealed interface MediatorState {
    data object Loading : MediatorState
    data object Error : MediatorState
    data object Done : MediatorState
}

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val application: Application,
    private val messageSource: MessageSource?,
    private val database: AppDatabase,
    private val networkMessageRepository: NetworkMessageRepository,
    private val mediatorStateFlow: MutableStateFlow<MediatorState>
) : RemoteMediator<Int, MessageEntity>() {
    private val messageDao = database.messageDao()

    // https://developer.android.com/topic/libraries/architecture/paging/v3-network-db#item-keys
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        if (messageSource == null) {
            return MediatorResult.Error(
                IllegalStateException("Cannot load a data without a defined source")
            )
        }

        return try {
            val lastKnownId: Int? = when (loadType) {
                LoadType.REFRESH -> {
                    mediatorStateFlow.emit(MediatorState.Loading)
                    null
                }

                LoadType.PREPEND -> {
                    mediatorStateFlow.emit(MediatorState.Done)
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    mediatorStateFlow.emit(MediatorState.Loading)

                    database.withTransaction {
                        when (messageSource) {
                            is MessageSource.ChannelOrUser -> messageDao.getMax(channelOrUsername = messageSource.channelOrUser)
                            is MessageSource.Inbox -> messageDao.getMaxInbox(
                                myUsername = messageSource.myUsername,
                                anotherUsername = messageSource.anotherUsername
                            )
                        }
                    }
                }
            }

            val pageKey = lastKnownId ?: 0
            val pageSize = state.config.pageSize

            val response = when (messageSource) {
                is MessageSource.ChannelOrUser -> networkMessageRepository.getFromChannel(
                    limit = pageSize,
                    channelName = messageSource.channelOrUser,
                    lastKnownId = pageKey
                )

                is MessageSource.Inbox -> networkMessageRepository.getUserInbox(
                    limit = pageSize,
                    username = messageSource.myUsername,
                    lastKnownId = pageKey
                ).filter {
                    (it.to == messageSource.myUsername && it.from == messageSource.anotherUsername) ||
                            (it.to == messageSource.anotherUsername && it.from == messageSource.myUsername)
                }
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    when (messageSource) {
                        is MessageSource.ChannelOrUser -> messageDao.deleteAllBy(channelOrUsername = messageSource.channelOrUser)
                        is MessageSource.Inbox -> messageDao.deleteAllByInbox(
                            myUsername = messageSource.myUsername,
                            anotherUsername = messageSource.anotherUsername
                        )
                    }
                }

                val entities = response.map { it.toMessageEntity() }
                messageDao.insertAll(entities)
            }

            val endOfPaginationReached = isEndOfPaginationReached(
                response = response
            )

            if (endOfPaginationReached) {
                mediatorStateFlow.emit(MediatorState.Done)
            }
            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: Exception) {
            Toast.makeText(
                application.applicationContext,
                "MediatorResult.Error. Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            mediatorStateFlow.emit(MediatorState.Error)
            MediatorResult.Error(e)
        }
    }
}
