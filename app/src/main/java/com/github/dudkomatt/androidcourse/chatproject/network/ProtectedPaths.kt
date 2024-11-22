package com.github.dudkomatt.androidcourse.chatproject.network

object ProtectedPaths {
    val methodToPathPrefix: Set<Pair<String, String>> = setOf(
        HttpRequestType.GET.name to "/inbox/",
        HttpRequestType.POST.name to "/1ch",
        HttpRequestType.POST.name to "/messages",
    )
}

enum class HttpRequestType {
    GET,
    POST,
}
