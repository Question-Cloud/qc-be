package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.exception.InvalidPaymentException;
import com.eager.questioncloud.pg.exception.InvalidPaymentIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class PaymentExceptionHandler {
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

    @ExceptionHandler(InvalidPaymentIdException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPaymentIdException() {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse
                    .builder()
                    .status(404)
                    .message("존재하지 않는 결제입니다.")
                    .build()
            );
    }
}
