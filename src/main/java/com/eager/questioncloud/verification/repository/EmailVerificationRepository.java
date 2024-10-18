package com.eager.questioncloud.verification.repository;

import com.eager.questioncloud.verification.dto.EmailVerificationWithUser;
import com.eager.questioncloud.verification.model.EmailVerification;
import com.eager.questioncloud.verification.model.EmailVerificationType;

public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getForNotVerifiedUser(Long userId);

    EmailVerificationWithUser getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
