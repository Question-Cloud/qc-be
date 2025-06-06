package com.eager.questioncloud.core.domain.verification.implement

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.infrastructure.repository.EmailVerificationRepository
import com.eager.questioncloud.core.domain.verification.model.EmailVerification
import com.eager.questioncloud.core.domain.verification.model.EmailVerification.Companion.create
import org.springframework.stereotype.Component

@Component
class EmailVerificationProcessor(
    private val emailVerificationRepository: EmailVerificationRepository
) {
    fun getByResendToken(resendToken: String): EmailVerification {
        return emailVerificationRepository.getForResend(resendToken)
    }

    fun createEmailVerification(
        userId: Long,
        email: String,
        emailVerificationType: EmailVerificationType
    ): EmailVerification {
        return emailVerificationRepository.save(create(userId, email, emailVerificationType))
    }

    fun verifyEmailVerification(token: String, emailVerificationType: EmailVerificationType): EmailVerification {
        val emailVerification = emailVerificationRepository.get(token, emailVerificationType)
        emailVerification.verify()
        emailVerificationRepository.update(emailVerification)
        return emailVerification
    }
}
