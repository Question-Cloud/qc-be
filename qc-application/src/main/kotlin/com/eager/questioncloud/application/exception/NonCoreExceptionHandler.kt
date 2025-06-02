package com.eager.questioncloud.application.exception

import com.eager.ApiTransactionContextHolder
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(2)
class NonCoreExceptionHandler(
    private val slackNotifier: ExceptionSlackNotifier
) {
    @ExceptionHandler(Exception::class)
    protected fun handler(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val transactionId = ApiTransactionContextHolder.get().transactionId
        slackNotifier.sendApiException(e, transactionId, request.requestURI, request.method)
        ApiTransactionContextHolder.markException()
        return ErrorResponse.toResponse(CoreException(Error.INTERNAL_SERVER_ERROR))
    }

    @ExceptionHandler(ServletException::class)
    protected fun handler(e: ServletException, request: HttpServletRequest) {
        ApiTransactionContextHolder.markException()
        throw e
    }
}