package com.eager.questioncloud.exception;

import com.eager.questioncloud.mail.CreateUserEmailVerificationProcessor;
import com.eager.questioncloud.mail.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class NotVerificationUserExceptionHandler {
    private final CreateUserEmailVerificationProcessor createUserEmailVerificationProcessor;

    @ExceptionHandler(NotVerificationUserException.class)
    protected ResponseEntity<NotVerificationUserResponse> handleCustomException(NotVerificationUserException e) {
        EmailVerification emailVerification = createUserEmailVerificationProcessor.getForNotVerifiedUser(e.getUser().getUid());
        return NotVerificationUserResponse.toResponse(emailVerification.getResendToken());
    }
}
