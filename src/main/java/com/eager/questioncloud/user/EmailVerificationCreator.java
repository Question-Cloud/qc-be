package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationCreator {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification append(EmailVerification emailVerification) {
        return emailVerificationRepository.append(emailVerification);
    }
}
