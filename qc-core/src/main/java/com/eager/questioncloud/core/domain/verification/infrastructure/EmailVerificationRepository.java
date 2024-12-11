package com.eager.questioncloud.core.domain.verification.infrastructure;


import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;

public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getCreateUserVerification(Long userId);

    EmailVerification getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
