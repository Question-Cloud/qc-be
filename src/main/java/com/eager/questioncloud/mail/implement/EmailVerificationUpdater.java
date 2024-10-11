package com.eager.questioncloud.mail.implement;

import com.eager.questioncloud.mail.repository.EmailVerificationRepository;
import com.eager.questioncloud.mail.domain.EmailVerification;
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
