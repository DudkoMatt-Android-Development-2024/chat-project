package com.github.dudkomatt.androidcourse.chatproject.data

import android.util.Log
import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.RootViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.RequestBody

class NetworkMessagePostRepository(
    private val retrofitMessageApi: MessageApi,
    private val rootViewModel: RootViewModel,
    private val database: AppDatabase
) {
    private val messageDao = database.messageDao()

    suspend fun postMessage(
        textMessage: TextMessageRequest,
    ): Int? {
        val token = rootViewModel.uiState.value.token ?: return null
        val response = retrofitMessageApi.postMessage(
            token = token,
            textMessage = textMessage
        )

        database.withTransaction {
            messageDao.insertAll(
                listOf(
                    MessageEntity(
                        id = response,
                        from = textMessage.from,
                        to = textMessage.to,
                        imageUrl = null,
                        text = textMessage.data.text?.text,
                        time = System.currentTimeMillis()
                    )
                )
            )
        }

        return response
    }

    suspend fun postMessage(
        textMessage: TextMessageRequest,
        image: RequestBody,
    ): Int? {
        val token = rootViewModel.uiState.value.token ?: return null
        val response = retrofitMessageApi.postMessage(
            token = token,
            textMessage = textMessage,
            image = image
        )

        database.withTransaction {
            messageDao.insertAll(
                listOf(
                    MessageEntity(
                        id = response,
                        from = textMessage.from,
                        to = textMessage.to,
                        imageUrl = null,
                        text = textMessage.data.text?.text + " <THIS MESSAGE CONTAINS AN IMAGE - REFRESH CHAT TO VIEW IT>",
                        time = System.currentTimeMillis()
                    )
                )
            )
        }

        return response
    }
}
