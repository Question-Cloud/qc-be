package com.eager.questioncloud.exception;

import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationReader;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.exception.NotVerificationUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class NotVerificationUserExceptionHandler {
    private final EmailVerificationReader emailVerificationReader;

    @ExceptionHandler(NotVerificationUserException.class)
    protected ResponseEntity<NotVerificationUserResponse> handleCustomException(NotVerificationUserException e) {
        EmailVerification emailVerification = emailVerificationReader.getForNotVerifiedUser(e.getUser().getUid());
        return NotVerificationUserResponse.toResponse(emailVerification.getResendToken());
    }
}