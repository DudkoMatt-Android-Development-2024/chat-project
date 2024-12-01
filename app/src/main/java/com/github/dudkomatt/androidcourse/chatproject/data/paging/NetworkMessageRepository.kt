package com.github.dudkomatt.androidcourse.chatproject.data.paging

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
    private val retrofitMessageApi: MessageApi,
    private val userSessionRepository: UserSessionRepository
) {
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
