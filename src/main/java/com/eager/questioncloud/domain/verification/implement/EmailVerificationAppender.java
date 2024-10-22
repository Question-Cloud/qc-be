package com.eager.questioncloud.domain.verification.implement;

import com.eager.questioncloud.domain.verification.model.EmailVerification;
import com.eager.questioncloud.domain.verification.repository.EmailVerificationRepository;
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
