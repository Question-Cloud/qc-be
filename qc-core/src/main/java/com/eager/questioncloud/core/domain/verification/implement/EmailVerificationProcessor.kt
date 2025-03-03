package com.eager.questioncloud.core.domain.verification.implement;

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.infrastructure.repository.EmailVerificationRepository;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationProcessor {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification getByResendToken(String resendToken) {
        return emailVerificationRepository.getForResend(resendToken);
    }

    public EmailVerification createEmailVerification(Long userId, String email, EmailVerificationType emailVerificationType) {
        return emailVerificationRepository.save(EmailVerification.create(userId, email, emailVerificationType));
    }

    public EmailVerification verifyEmailVerification(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationRepository.get(token, emailVerificationType);
        emailVerification.verify();
        return emailVerificationRepository.save(emailVerification);
    }
}
