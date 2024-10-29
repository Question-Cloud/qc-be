package com.eager.questioncloud.exception;

import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import com.eager.questioncloud.core.exception.InvalidPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class InvalidPaymentExceptionHandler {
    private final PortoneAPI portoneAPI;

    @ExceptionHandler(InvalidPaymentException.class)
    protected ResponseEntity<InvalidPaymentExceptionResponse> handleInvalidPaymentException(InvalidPaymentException e) {
        portoneAPI.cancel(e.getPortonePayment().getId());
        return InvalidPaymentExceptionResponse.toResponse();
    }
}
