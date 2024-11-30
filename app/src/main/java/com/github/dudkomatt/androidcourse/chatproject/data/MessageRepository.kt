package com.github.dudkomatt.androidcourse.chatproject.data

import com.github.dudkomatt.androidcourse.chatproject.exception.TokenMissingException
import com.github.dudkomatt.androidcourse.chatproject.model.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import okhttp3.RequestBody

// TODO - Здесь кэширование
class MessageRepository(
    private val userSessionRepository: UserSessionRepository,
    private val retrofitMessageApi: MessageApi
) {
    suspend fun getFrom1ch(
        limit: Int?,
        lastKnownId: Int?,
        reverse: Boolean?
    ): List<MessageModel> {
        return retrofitMessageApi.getFrom1ch(
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = reverse
        )
    }

    suspend fun getFromChannel(
        channelName: String,
        limit: Int?,
        lastKnownId: Int?,
        reverse: Boolean?
    ): List<MessageModel> {
        return retrofitMessageApi.getFromChannel(
            channelName = channelName,
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = reverse
        )
    }

    suspend fun getUserInbox(
        username: String,
        limit: Int?,
        lastKnownId: Int?,
        reverse: Boolean?
    ): List<MessageModel> {
        return retrofitMessageApi.getUserInbox(
            token = getToken(),
            username = username,
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = reverse
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
