package com.eager.questioncloud.logging.api

data class ApiRequest(
    val url: String,
    val method: String,
    val body: String,
    val clientIp: String,
    val headers: Map<String, String>,
    val params: Map<String, Array<String>>
)