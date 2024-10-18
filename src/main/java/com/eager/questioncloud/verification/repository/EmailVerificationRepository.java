package com.eager.questioncloud.verification.repository;

import com.eager.questioncloud.verification.domain.EmailVerification;
import com.eager.questioncloud.verification.domain.EmailVerificationType;
import com.eager.questioncloud.verification.dto.EmailVerificationWithUser;

public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getForNotVerifiedUser(Long userId);

    EmailVerificationWithUser getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
