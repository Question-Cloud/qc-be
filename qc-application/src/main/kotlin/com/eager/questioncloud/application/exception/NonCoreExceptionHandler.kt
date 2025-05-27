package com.eager.questioncloud.application.exception

import com.eager.ApiTransactionContextHolder
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class NonCoreExceptionHandler(
    private val slackNotifier: ExceptionSlackNotifier
) {
    @ExceptionHandler(RuntimeException::class)
    protected fun handler(e: Exception, request: HttpServletRequest) {
        val transactionId = ApiTransactionContextHolder.get().transactionId
        slackNotifier.sendApiException(e, transactionId, request.requestURI, request.method)
        throw e
    }
}