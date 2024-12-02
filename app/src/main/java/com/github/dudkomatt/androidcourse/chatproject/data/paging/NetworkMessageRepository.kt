package com.github.dudkomatt.androidcourse.chatproject.data.paging

import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi

sealed interface MessageSource {
    data class Inbox(val username: String) : MessageSource
    data class ChannelOrUser(val channelOrUser: String) : MessageSource
}

class NetworkMessagePagingRepository(
    private val retrofitMessageApi: MessageApi,
    private val token: String,
) {
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
            token = token,
            username = username,
            limit = limit,
            lastKnownId = lastKnownId,
            reverse = false
        )
    }

    companion object {
        fun isEndOfPaginationReached(response: List<Any>, loadSize: Int): Boolean {
            return response.isEmpty() || response.size < loadSize
        }
    }
}
