package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.exception.CoreException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class ErrorResponse {
    private int status;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponse(CoreException e) {
        return ResponseEntity
            .status(e.getError().getHttpStatus())
            .body(ErrorResponse
                .builder()
                .status(e.getError().getHttpStatus())
                .message(e.getError().getMessage())
                .build());
    }
}