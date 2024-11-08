package com.eager.questioncloud.storage.verification;

import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
    Optional<EmailVerificationEntity> findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        String token, EmailVerificationType emailVerificationType);

    Optional<EmailVerificationEntity> findByResendToken(String resendToken);
}
