package com.github.dudkomatt.androidcourse.chatproject.network

import com.github.dudkomatt.androidcourse.chatproject.data.RetrofitConfigs
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/addusr")
    @FormUrlEncoded
    suspend fun registerNewUser(@Field("name") name: String): Response<ResponseBody>

    @POST("/login")
    suspend fun loginPost(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("/logout")
    suspend fun logoutPost(@Header(RetrofitConfigs.X_AUTH_TOKEN_HEADER) token: String)
}
