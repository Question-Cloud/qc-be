package com.eager.questioncloud.user.repository

import com.eager.questioncloud.user.entity.EmailVerificationEntity
import com.eager.questioncloud.user.enums.EmailVerificationType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailVerificationJpaRepository : JpaRepository<EmailVerificationEntity, String> {
    fun findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        token: String,
        emailVerificationType: EmailVerificationType
    ): Optional<EmailVerificationEntity>
    
    fun findByResendToken(resendToken: String): Optional<EmailVerificationEntity>
}
