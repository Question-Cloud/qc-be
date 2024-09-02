package com.eager.questioncloud.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationReader {
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerification get(String token, EmailVerificationType emailVerificationType) {
        return emailVerificationRepository.get(token, emailVerificationType);
    }

    public EmailVerification getForNotVerifiedUser(Long userId) {
        return emailVerificationRepository.getForNotVerifiedUser(userId);
    }

    public EmailVerificationWithUser getForResend(String resendToken) {
        return emailVerificationRepository.getForResend(resendToken);
    }
}
