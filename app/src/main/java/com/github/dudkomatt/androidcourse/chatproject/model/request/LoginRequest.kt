package com.github.dudkomatt.androidcourse.chatproject.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val name: String,
    val pwd: String,
)
