package com.eager.questioncloud.common.http

sealed class HttpClientException : RuntimeException() {
    class Response4xxException() : HttpClientException()
    class Response5xxException() : HttpClientException()
}