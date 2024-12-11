package com.eager.questioncloud.core.domain.verification.infrastructure;

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
    Optional<EmailVerificationEntity> findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        String token, EmailVerificationType emailVerificationType);

    Optional<EmailVerificationEntity> findByResendToken(String resendToken);
}
