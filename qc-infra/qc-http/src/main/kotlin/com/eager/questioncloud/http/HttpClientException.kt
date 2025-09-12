package com.eager.questioncloud.http

sealed class HttpClientException : RuntimeException() {
    class Response4xxException() : HttpClientException()
    class Response5xxException() : HttpClientException()
}