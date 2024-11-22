package com.github.dudkomatt.androidcourse.chatproject.network

import com.github.dudkomatt.androidcourse.chatproject.model.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.request.TextMessageRequest
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageApi {

    // Receive data - GET
    // ==================

    @GET("/1ch")
    suspend fun getFrom1ch(@Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    @GET("/channel/{channel_name}")
    suspend fun getFromChannel(@Path("channel_name") channelName: String, @Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    // TODO - Auth header
    @GET("/inbox/{username}")
    suspend fun getUserInbox(@Path("username") username: String, @Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    // Send data - POST
    // ================

    // TODO - Auth header
    @POST("/1ch")
    suspend fun postTo1ch(@Body textMessage: TextMessageRequest): Int

    // TODO - Auth header
    @POST("/messages")
    suspend fun postMessage(@Body textMessage: TextMessageRequest): Int

    // TODO - Auth header
    // TODO - check msg / Content-Type=multipart/form-data
    @Multipart
    @POST("/1ch")
    suspend fun postTo1ch(@Part("msg") textMessage: TextMessageRequest, @Part("picture") image: RequestBody): Int

    // TODO - Auth header
    // TODO - check msg / Content-Type=multipart/form-data
    @Multipart
    @POST("/messages")
    suspend fun postMessage(@Part("msg") textMessage: TextMessageRequest, @Part("picture") image: RequestBody): Int
}
