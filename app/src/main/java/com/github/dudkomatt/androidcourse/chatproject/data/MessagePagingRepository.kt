package com.github.dudkomatt.androidcourse.chatproject.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.dudkomatt.androidcourse.chatproject.exception.TokenMissingException
import com.github.dudkomatt.androidcourse.chatproject.model.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import okhttp3.RequestBody

class QueryParameters(
    val lastKnownId: Int = 0,
    val reverse: Boolean = false
) {
    fun next(lastKnownId: Int): QueryParameters {
        return QueryParameters(
            lastKnownId = lastKnownId,
            reverse = reverse,
        )
    }
}

sealed interface MessageSource {
    data class Inbox(val username: String) : MessageSource
    data class ChannelOrUser(val channelOrUser: String) : MessageSource
}

// TODO - Здесь кэширование?
class MessagePagingRepository(
    private val userSessionRepository: UserSessionRepository,
    private val retrofitMessageApi: MessageApi,
    private val messageSource: MessageSource?
) : PagingSource<QueryParameters, MessageModel>() {

    override fun getRefreshKey(state: PagingState<QueryParameters, MessageModel>): QueryParameters? {
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

    override suspend fun load(params: LoadParams<QueryParameters>): LoadResult<QueryParameters, MessageModel> {
        // https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data

        try {
            val pageParameters = params.key ?: QueryParameters()

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
                    page = pageParameters
                )

                is MessageSource.Inbox -> getUserInbox(
                    limit = params.loadSize,
                    username = messageSource.username,
                    page = pageParameters
                )
            }

            return LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward
                nextKey = if (response.isEmpty() || response.size < params.loadSize) null
                else pageParameters.next(response.maxOf { it.id })
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    suspend fun getFrom1ch(limit: Int, page: QueryParameters): List<MessageModel> {
        return retrofitMessageApi.getFrom1ch(
            limit = limit,
            lastKnownId = page.lastKnownId,
            reverse = page.reverse
        )
    }

    suspend fun getFromChannel(
        limit: Int,
        channelName: String,
        page: QueryParameters
    ): List<MessageModel> {
        return retrofitMessageApi.getFromChannel(
            channelName = channelName,
            limit = limit,
            lastKnownId = page.lastKnownId,
            reverse = page.reverse
        )
    }

    suspend fun getUserInbox(
        limit: Int,
        username: String,
        page: QueryParameters
    ): List<MessageModel> {
        return retrofitMessageApi.getUserInbox(
            token = getToken(),
            username = username,
            limit = limit,
            lastKnownId = page.lastKnownId,
            reverse = page.reverse
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
}
