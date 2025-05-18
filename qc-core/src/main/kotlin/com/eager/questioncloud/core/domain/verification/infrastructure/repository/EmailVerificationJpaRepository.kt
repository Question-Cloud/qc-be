package com.eager.questioncloud.core.domain.verification.infrastructure.repository

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.infrastructure.entity.EmailVerificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailVerificationJpaRepository : JpaRepository<EmailVerificationEntity, String> {
    fun findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        token: String,
        emailVerificationType: EmailVerificationType
    ): Optional<EmailVerificationEntity>

    fun findByResendToken(resendToken: String): Optional<EmailVerificationEntity>
}
