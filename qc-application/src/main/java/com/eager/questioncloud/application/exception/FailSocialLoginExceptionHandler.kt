package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.social.FailSocialLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FailSocialLoginExceptionHandler {
    @ExceptionHandler(FailSocialLoginException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidPaymentException(FailSocialLoginException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse
                    .builder()
                    .status(400)
                    .message("소셜 로그인 요청 실패")
                    .build()
            );
    }
}
