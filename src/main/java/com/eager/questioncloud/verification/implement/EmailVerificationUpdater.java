package com.eager.questioncloud.verification.implement;

import com.eager.questioncloud.verification.model.EmailVerification;
import com.eager.questioncloud.verification.repository.EmailVerificationRepository;
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
