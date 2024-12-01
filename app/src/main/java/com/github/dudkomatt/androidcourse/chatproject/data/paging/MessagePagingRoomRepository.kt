package com.github.dudkomatt.androidcourse.chatproject.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.dudkomatt.androidcourse.chatproject.data.paging.NetworkMessagePagingRepository.Companion.isEndOfPaginationReached
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.room.MessageDao

class MessagePagingRoomRepository(
    private val messageSource: MessageSource?,
    private val messageDao: MessageDao
) : PagingSource<Int, MessageEntity>() {

    override fun getRefreshKey(state: PagingState<Int, MessageEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageEntity> {
        try {
            val lastKnownId = params.key ?: 0

            if (messageSource == null) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val channelOrUsername = when (messageSource) {
                is MessageSource.ChannelOrUser -> messageSource.channelOrUser
                is MessageSource.Inbox -> messageSource.username
            }

            val response = messageDao.getBy(
                channelOrUsername = channelOrUsername,
                lastKnownId = lastKnownId,
                limit = params.loadSize
            )

            return LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward
                nextKey = if (isEndOfPaginationReached(response, params.loadSize)) null
                else response.maxOf { it.id }
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
