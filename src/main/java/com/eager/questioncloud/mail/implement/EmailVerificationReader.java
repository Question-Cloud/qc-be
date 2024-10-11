package com.eager.questioncloud.mail.implement;

import com.eager.questioncloud.mail.repository.EmailVerificationRepository;
import com.eager.questioncloud.mail.dto.EmailVerificationWithUser;
import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
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
