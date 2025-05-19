package com.eager

data class ApiRequest(
    val url: String,
    val method: String,
    val body: String,
    val clientIp: String,
    val headers: Map<String, String>,
    val params: Map<String, Array<String>>
)