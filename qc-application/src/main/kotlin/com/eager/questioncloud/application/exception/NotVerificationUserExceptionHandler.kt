package com.eager.questioncloud.application.exception

import com.eager.questioncloud.core.exception.NotVerificationUserException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class NotVerificationUserExceptionHandler {
    @ExceptionHandler(NotVerificationUserException::class)
    protected fun handleCustomException(e: NotVerificationUserException?): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(403, "이메일 인증을 완료해주세요."))
    }
}
