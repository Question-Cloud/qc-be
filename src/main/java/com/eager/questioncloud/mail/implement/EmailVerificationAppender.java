package com.eager.questioncloud.mail.implement;

import com.eager.questioncloud.mail.repository.EmailVerificationRepository;
import com.eager.questioncloud.mail.domain.EmailVerification;
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
