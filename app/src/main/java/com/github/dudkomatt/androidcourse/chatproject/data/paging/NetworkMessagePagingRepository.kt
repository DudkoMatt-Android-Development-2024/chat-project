package com.github.dudkomatt.androidcourse.chatproject.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.dudkomatt.androidcourse.chatproject.data.UserSessionRepository
import com.github.dudkomatt.androidcourse.chatproject.exception.TokenMissingException
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import okhttp3.RequestBody

sealed interface MessageSource {
    data class Inbox(val username: String) : MessageSource
    data class ChannelOrUser(val channelOrUser: String) : MessageSource
}

class NetworkMessagePagingRepository(
    private val messageSource: MessageSource?,
    private val retrofitMessageApi: MessageApi,
    private val userSessionRepository: UserSessionRepository
) : PagingSource<Int, MessageModel>() {

    override fun getRefreshKey(state: PagingState<Int, MessageModel>): Int? {
        // https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data

        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.

        // Note: can calculate only forward, can rely only on prevKey

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageModel> {
        // https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data

        try {
            val lastKnownId = params.key ?: 0

            if (messageSource == null) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val response = when (messageSource) {
                is MessageSource.ChannelOrUser -> getFromChannel(
                    limit = params.loadSize,
                    channelName = messageSource.channelOrUser,
                    lastKnownId = lastKnownId
                )

                is MessageSource.Inbox -> getUserInbox(
                    limit = params.loadSize,
                    username = messageSource.username,
                    lastKnownId = lastKnownId
                )
            }

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

    suspend fun getFrom1ch(limit: Int, lastKnownId: Int): List<MessageModel> {
        return retrofitMessageApi.getFrom1ch(
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = false
        )
    }

    suspend fun getFromChannel(
        limit: Int,
        channelName: String,
        lastKnownId: Int
    ): List<MessageModel> {
        return retrofitMessageApi.getFromChannel(
            channelName = channelName,
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = false
        )
    }

    suspend fun getUserInbox(
        limit: Int,
        username: String,
        lastKnownId: Int
    ): List<MessageModel> {
        return retrofitMessageApi.getUserInbox(
            token = getToken(),
            username = username,
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = false
        )
    }

    suspend fun postTo1ch(textMessage: TextMessageRequest): Int {
        return retrofitMessageApi.postTo1ch(token = getToken(), textMessage = textMessage)
    }

    suspend fun postMessage(textMessage: TextMessageRequest): Int {
        return retrofitMessageApi.postMessage(
            token = getToken(),
            textMessage = textMessage
        )
    }

    suspend fun postMessage(
        textMessage: TextMessageRequest,
        image: RequestBody
    ): Int {
        return retrofitMessageApi.postMessage(
            token = getToken(),
            textMessage = textMessage,
            image = image
        )
    }

    private suspend fun getToken(): String =
        userSessionRepository.getToken() ?: throw TokenMissingException()

    companion object {
        fun isEndOfPaginationReached(response: List<Any>, loadSize: Int): Boolean {
            return response.isEmpty() || response.size < loadSize
        }
    }
}
