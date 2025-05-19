package com.eager

import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class ApiTransactionContext(
    val transactionId: String = UUID.randomUUID().toString(),
    var apiRequest: ApiRequest? = null,
    val methodRecordContext: MethodRecordContext = MethodRecordContext(),
    val startAt: LocalDateTime = LocalDateTime.now(),
    var endAt: LocalDateTime = LocalDateTime.now(),
    var runningTime: Long = 0
) {
    fun startMethod(methodName: String): Int {
        return methodRecordContext.startMethod(methodName)
    }

    fun endMethod(target: Int) {
        methodRecordContext.endMethod(target)
    }

    fun end() {
        endAt = LocalDateTime.now()
        runningTime = Duration.between(startAt, endAt).toMillis()
        methodRecordContext.end()
    }

    fun markException(targetMethodRecordIndex: Int, exceptionMessage: String?) {
        methodRecordContext.markException(targetMethodRecordIndex, exceptionMessage)
    }

    fun loggingApiRequest(apiRequest: ApiRequest) {
        this.apiRequest = apiRequest
    }
}