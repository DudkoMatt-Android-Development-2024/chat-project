package com.github.dudkomatt.androidcourse.chatproject.data.paging

import android.app.Application
import android.util.Log
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

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val application: Application,
    private val messageSource: MessageSource?,
    private val database: AppDatabase,
    private val networkMessageRepository: NetworkMessageRepository,
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
//                    Log.d("TAG", "load: REFRESH")
                    null
                }

                LoadType.PREPEND -> {
//                    Log.d("TAG", "load: PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
//                    Log.d("TAG", "load: APPEND")

                    val toUsernameOrChannel = when (messageSource) {
                        is MessageSource.ChannelOrUser -> messageSource.channelOrUser
                        is MessageSource.Inbox -> messageSource.username
                    }

                    database.withTransaction {
                        messageDao.getMax(toUsernameOrChannel)
                    }

//                    var max: Int = -1
//                    database.withTransaction {
//                        max = messageDao.getMax(toUsernameOrChannel)
//                    }
//
//
//                    Log.d("TAG", "load: APPEND - max: ${max}")
//
//                    Log.d("TAG", "load: APPEND")
//
//                    val lastItem = state.lastItemOrNull()
//
//                    if (lastItem == null) {
//                        Log.d("TAG", "load: APPEND EXIT - last item is null")
//
//                        return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
//                    }
//
//                    Log.d("TAG", "load: ${state}")
//                    Log.d("TAG", "load: ${lastItem.id}")
//
//                    lastItem.id
                }
            }

            val pageKey = lastKnownId ?: 0

            val toUsernameOrChannel = when (messageSource) {
                is MessageSource.ChannelOrUser -> messageSource.channelOrUser
                is MessageSource.Inbox -> messageSource.username
            }

            val pageSize = state.config.pageSize

            Log.d("TAG", "load: BEFORE CALL NETWORK")

            val response = when (messageSource) {
                is MessageSource.ChannelOrUser -> networkMessageRepository.getFromChannel(
                    limit = pageSize,
                    channelName = toUsernameOrChannel,
                    lastKnownId = pageKey
                )

                is MessageSource.Inbox -> networkMessageRepository.getUserInbox(
                    limit = pageSize,
                    username = toUsernameOrChannel,
                    lastKnownId = pageKey
                )
            }

//            Log.d("TAG", "load: AFTER CALL NETWORK")
//
//            Log.d("TAG", "load: ${response}")
//            Log.d("TAG", "load: ${response.size}")
//            if (response.size > 0)
//            Log.d("TAG", "load: ${response[response.size - 1].id}")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    messageDao.deleteAllBy(toUsernameOrChannel)
                }

                val entities = response.map { it.toMessageEntity() }
                messageDao.insertAll(entities)
            }

            val endOfPaginationReached = isEndOfPaginationReached(
                response = response
            )
            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: Exception) {
//            Log.d("TAG", "load: ERROR MEDIATOR ${e.message}")
            Toast.makeText(
                application.applicationContext,
                "MediatorResult.Error. Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            MediatorResult.Error(e)
        }
    }
}
