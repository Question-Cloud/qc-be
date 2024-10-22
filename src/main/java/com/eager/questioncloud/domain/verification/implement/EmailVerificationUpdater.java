package com.eager.questioncloud.domain.verification.implement;

import com.eager.questioncloud.domain.verification.model.EmailVerification;
import com.eager.questioncloud.domain.verification.repository.EmailVerificationRepository;
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
