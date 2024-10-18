package com.eager.questioncloud.verification.implement;

import com.eager.questioncloud.verification.model.EmailVerification;
import com.eager.questioncloud.verification.repository.EmailVerificationRepository;
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
