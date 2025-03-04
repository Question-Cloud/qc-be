package com.eager.questioncloud.application.exception

import com.eager.questioncloud.social.FailSocialLoginException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class FailSocialLoginExceptionHandler {
    @ExceptionHandler(FailSocialLoginException::class)
    protected fun handleInvalidPaymentException(e: FailSocialLoginException?): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(400, "소셜 로그인 요청 실패"))
    }
}
