package com.eager.questioncloud.verification.implement;

import com.eager.questioncloud.verification.repository.EmailVerificationRepository;
import com.eager.questioncloud.verification.domain.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationAppender {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification append(EmailVerification emailVerification) {
        return emailVerificationRepository.save(emailVerification);
    }
}
