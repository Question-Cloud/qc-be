package com.eager

import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class ApiTransactionContext(
    val transactionId: String = UUID.randomUUID().toString(),
    var apiRequest: ApiRequest? = null,
    var apiResponse: ApiResponse? = null,
    var isMarkedException: Boolean = false,
    val startAt: LocalDateTime = LocalDateTime.now(),
    var endAt: LocalDateTime = LocalDateTime.now(),
    var runningTime: Long = 0
) {
    fun end() {
        endAt = LocalDateTime.now()
        runningTime = Duration.between(startAt, endAt).toMillis()
    }

    fun markException() {
        isMarkedException = true
    }

    fun loggingApiRequest(apiRequest: ApiRequest) {
        this.apiRequest = apiRequest
    }

    fun loggingApiResponse(apiResponse: ApiResponse) {
        this.apiResponse = apiResponse
    }
}