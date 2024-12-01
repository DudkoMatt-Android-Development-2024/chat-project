package com.github.dudkomatt.androidcourse.chatproject.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessagePagingRepository.Companion.isEndOfPaginationReached
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.toMessageEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val messageSource: MessageSource?,
    private val database: AppDatabase,
    private val networkMessagePagingRepository: NetworkMessagePagingRepository,
) : RemoteMediator<Int, MessageEntity>() {
    private val messageDao = database.messageDao()

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
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val lastKnownId: Int? = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    lastItem.id
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.

            val pageKey = lastKnownId ?: 0

            val toUsernameOrChannel = when (messageSource) {
                is MessageSource.ChannelOrUser -> messageSource.channelOrUser
                is MessageSource.Inbox -> messageSource.username
            }

            val pageSize = state.config.pageSize

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

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
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
