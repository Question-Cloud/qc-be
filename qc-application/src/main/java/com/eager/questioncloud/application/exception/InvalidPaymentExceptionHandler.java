package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.exception.InvalidPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class InvalidPaymentExceptionHandler {
    @ExceptionHandler(InvalidPaymentException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPaymentException() {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse
                    .builder()
                    .status(400)
                    .message("결제 금액 오류")
                    .build()
            );
    }
}
