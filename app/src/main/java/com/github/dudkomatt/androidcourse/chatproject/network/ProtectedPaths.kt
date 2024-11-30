package com.github.dudkomatt.androidcourse.chatproject.network

object ProtectedPaths {
    val methodToPathPrefix: Map<String, Set<String>> = mapOf(
        "/inbox/" to setOf(HttpRequestType.GET.name),
        "/1ch" to setOf(HttpRequestType.POST.name),
        "/messages" to setOf(HttpRequestType.POST.name),
    )
}

enum class HttpRequestType {
    GET,
    POST,
}
