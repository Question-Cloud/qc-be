package com.eager.questioncloud.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class InvalidPaymentExceptionResponse {
    private String message;

    public static ResponseEntity<InvalidPaymentExceptionResponse> toResponse() {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(InvalidPaymentExceptionResponse.builder()
                .message("결제 금액 오류")
                .build());
    }
}
