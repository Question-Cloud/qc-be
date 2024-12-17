package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.exception.CoreException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CoreException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CoreException e) {
        return ErrorResponse.toResponse(e);
    }
}
