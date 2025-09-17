package com.eager.questioncloud.common.http

interface HttpClient {
    fun <T> get(req: HttpRequest, valueType: Class<T>): T
    fun <T> post(req: HttpRequest, valueType: Class<T>): T
    fun post(req: HttpRequest)
}