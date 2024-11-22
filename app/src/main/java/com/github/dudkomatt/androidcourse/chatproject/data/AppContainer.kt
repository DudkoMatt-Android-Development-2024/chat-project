package com.github.dudkomatt.androidcourse.chatproject.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AppContainer {
    private val baseUrl = "https://faerytea.name:8008"
//    private val okHttpClient: OkHttpClient  // TODO

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
//        .client(okHttpClient)  // TODO
        .build()
}
