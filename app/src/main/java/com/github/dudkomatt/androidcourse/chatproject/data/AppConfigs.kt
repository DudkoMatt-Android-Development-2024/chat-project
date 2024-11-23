package com.github.dudkomatt.androidcourse.chatproject.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object AppConfigs {
    private const val BASE_URL = "https://faerytea.name:8008"
//    private val okHttpClient: OkHttpClient  // TODO

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
//        .client(okHttpClient)  // TODO
        .build()
}
