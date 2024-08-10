package com.eager.questioncloud.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationReader {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification find(String token, EmailVerificationType emailVerificationType) {
        return emailVerificationRepository.find(token, emailVerificationType);
    }
}
