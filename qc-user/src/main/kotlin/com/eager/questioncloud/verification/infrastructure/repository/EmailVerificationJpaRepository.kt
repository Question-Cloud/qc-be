package com.eager.questioncloud.verification.infrastructure.repository

import com.eager.questioncloud.verification.enums.EmailVerificationType
import com.eager.questioncloud.verification.infrastructure.entity.EmailVerificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailVerificationJpaRepository : JpaRepository<EmailVerificationEntity, String> {
    fun findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        token: String,
        emailVerificationType: EmailVerificationType
    ): Optional<EmailVerificationEntity>

    fun findByResendToken(resendToken: String): Optional<EmailVerificationEntity>
}
