package com.eager.questioncloud.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationAppender {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification append(EmailVerification emailVerification) {
        return emailVerificationRepository.append(emailVerification);
    }
}
