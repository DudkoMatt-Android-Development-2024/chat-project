package com.github.dudkomatt.androidcourse.chatproject.network

import retrofit2.http.GET
import retrofit2.http.Path

interface InfoApi {
    @GET("/channels")
    suspend fun getChannels(): List<String>

    @GET("/users")
    suspend fun getUsers(): List<String>

    // TODO - Может быть не List, а что-то другое
    @GET("/typing/{channel_name}")
    suspend fun getTypingInChannel(@Path("channel_name") channelName: String): List<String>
}
