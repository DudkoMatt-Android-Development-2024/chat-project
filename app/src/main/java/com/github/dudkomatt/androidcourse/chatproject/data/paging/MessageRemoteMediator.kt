package com.github.dudkomatt.androidcourse.chatproject.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessagePagingRepository.Companion.isEndOfPaginationReached
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.toMessageEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val messageSource: MessageSource?,
    private val database: AppDatabase,
    private val networkMessagePagingRepository: NetworkMessagePagingRepository,
    private val pagingConfig: PagingConfig
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
                    null
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastPage = state.pages.lastOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    lastPage.data.maxOfOrNull { it.id }
                }
            }

            val pageKey = lastKnownId ?: 0

            val toUsernameOrChannel = when (messageSource) {
                is MessageSource.ChannelOrUser -> messageSource.channelOrUser
                is MessageSource.Inbox -> messageSource.username
            }

            val pageSize = pagingConfig.pageSize

            val response = when (messageSource) {
                is MessageSource.ChannelOrUser -> networkMessagePagingRepository.getFromChannel(
                    limit = pageSize,
                    channelName = toUsernameOrChannel,
                    lastKnownId = pageKey
                )

                is MessageSource.Inbox -> networkMessagePagingRepository.getUserInbox(
                    limit = pageSize,
                    username = toUsernameOrChannel,
                    lastKnownId = pageKey
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    messageDao.deleteAllBy(toUsernameOrChannel)
                }

                messageDao.insertAll(
                    response.map { it.toMessageEntity() }
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = isEndOfPaginationReached(
                    response = response,
                    loadSize = pageSize
                )
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
