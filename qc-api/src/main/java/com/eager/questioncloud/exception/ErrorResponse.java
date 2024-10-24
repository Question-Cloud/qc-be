package com.eager.questioncloud.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class ErrorResponse {
    private int status;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponse(CustomException e) {
        return ResponseEntity
            .status(e.getError().getHttpStatus())
            .body(ErrorResponse
                .builder()
                .status(e.getError().getHttpStatus())
                .message(e.getError().getMessage())
                .build());
    }
}