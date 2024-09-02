package com.eager.questioncloud.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationUpdater {
    private final EmailVerificationRepository emailVerificationRepository;

    public void verify(EmailVerification emailVerification) {
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }
}
