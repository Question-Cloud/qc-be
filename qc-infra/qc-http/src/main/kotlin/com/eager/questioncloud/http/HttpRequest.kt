package com.eager.questioncloud.http

class HttpRequest(
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val query: Map<String, String> = emptyMap(),
    val body: Any? = null,
    val form: Map<String, String> = emptyMap(),
    val contentType: ContentType? = null,
)