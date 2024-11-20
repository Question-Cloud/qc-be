package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.exception.InvalidPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class InvalidPaymentExceptionHandler {
    @ExceptionHandler(InvalidPaymentException.class)
    protected ResponseEntity<InvalidPaymentExceptionResponse> handleInvalidPaymentException(InvalidPaymentException e) {
        return InvalidPaymentExceptionResponse.toResponse();
    }
}
