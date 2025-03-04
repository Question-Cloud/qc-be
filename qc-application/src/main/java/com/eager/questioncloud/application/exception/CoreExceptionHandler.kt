package com.eager.questioncloud.application.exception

import com.eager.questioncloud.core.exception.CoreException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CoreExceptionHandler {
    @ExceptionHandler(CoreException::class)
    protected fun handleCustomException(e: CoreException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponse(e)
    }
}
