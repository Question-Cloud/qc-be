package com.eager.questioncloud.core.domain.verification.implement;

import com.eager.questioncloud.core.domain.verification.dto.EmailVerificationWithUser;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.repository.EmailVerificationRepository;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
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
