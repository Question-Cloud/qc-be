package com.eager.questioncloud.exception

import com.eager.questioncloud.common.exception.CoreException
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(1)
class CoreExceptionHandler {
    @ExceptionHandler(CoreException::class)
    protected fun handleCustomException(e: CoreException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponse(e)
    }
}
