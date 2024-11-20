package com.eager.questioncloud.application.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class NotVerificationUserResponse {
    private String message;
    private String resendToken;

    public static ResponseEntity<NotVerificationUserResponse> toResponse(String resendToken) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(NotVerificationUserResponse.builder()
                .message("메일 인증을 완료 해주세요.")
                .resendToken(resendToken)
                .build());
    }
}
