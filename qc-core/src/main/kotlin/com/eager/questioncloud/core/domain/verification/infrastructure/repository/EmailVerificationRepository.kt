package com.eager.questioncloud.core.domain.verification.infrastructure.repository

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.model.EmailVerification


interface EmailVerificationRepository {
    fun get(token: String, emailVerificationType: EmailVerificationType): EmailVerification

    fun getCreateUserVerification(userId: Long): EmailVerification

    fun getForResend(resendToken: String): EmailVerification

    fun save(emailVerification: EmailVerification): EmailVerification

    fun update(emailVerification: EmailVerification)
}
