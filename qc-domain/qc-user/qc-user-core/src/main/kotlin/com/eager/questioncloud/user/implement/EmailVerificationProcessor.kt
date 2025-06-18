package com.eager.questioncloud.user.implement

import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.infrastructure.repository.EmailVerificationRepository
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
        return emailVerificationRepository.save(EmailVerification.create(userId, email, emailVerificationType))
    }

    fun verifyEmailVerification(token: String, emailVerificationType: EmailVerificationType): EmailVerification {
        val emailVerification = emailVerificationRepository.get(token, emailVerificationType)
        emailVerification.verify()
        emailVerificationRepository.update(emailVerification)
        return emailVerification
    }
}
