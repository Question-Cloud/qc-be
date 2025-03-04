package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.exception.NotVerificationUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class NotVerificationUserExceptionHandler {
    @ExceptionHandler(NotVerificationUserException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(NotVerificationUserException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ErrorResponse
                    .builder()
                    .status(403)
                    .message("이메일 인증을 완료해주세요.")
                    .build()
            );
    }
}
