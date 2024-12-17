package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.domain.verification.infrastructure.EmailVerificationRepository;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.exception.NotVerificationUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class NotVerificationUserExceptionHandler {
    private final EmailVerificationRepository emailVerificationRepository;

    @ExceptionHandler(NotVerificationUserException.class)
    protected ResponseEntity<NotVerificationUserResponse> handleCustomException(NotVerificationUserException e) {
        EmailVerification emailVerification = emailVerificationRepository.getCreateUserVerification(e.getUserId());
        return NotVerificationUserResponse.toResponse(emailVerification.getResendToken());
    }
}
