package com.eager.questioncloud.verification.infrastructure.repository

import com.eager.questioncloud.verification.domain.EmailVerification
import com.eager.questioncloud.verification.enums.EmailVerificationType

interface EmailVerificationRepository {
    fun get(token: String, emailVerificationType: EmailVerificationType): EmailVerification

    fun getCreateUserVerification(userId: Long): EmailVerification

    fun getForResend(resendToken: String): EmailVerification

    fun save(emailVerification: EmailVerification): EmailVerification

    fun update(emailVerification: EmailVerification)
}
