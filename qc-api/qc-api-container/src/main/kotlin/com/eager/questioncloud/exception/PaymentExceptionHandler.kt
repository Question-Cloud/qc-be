package com.eager.questioncloud.exception

import com.eager.questioncloud.pg.exception.InvalidPaymentIdException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class PaymentExceptionHandler {
    @ExceptionHandler(InvalidPaymentIdException::class)
    protected fun handleInvalidPaymentIdException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(404, "존재하지 않는 결제입니다."))
    }
}
