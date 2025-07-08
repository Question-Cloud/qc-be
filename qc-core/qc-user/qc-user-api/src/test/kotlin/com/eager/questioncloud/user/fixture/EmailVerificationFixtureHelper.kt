package com.eager.questioncloud.user.fixture

import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.infrastructure.repository.EmailVerificationRepository

class EmailVerificationFixtureHelper {
    companion object {
        fun createEmailVerification(
            uid: Long,
            email: String,
            emailVerificationType: EmailVerificationType,
            emailVerificationRepository: EmailVerificationRepository
        ): EmailVerification {
            val emailVerification = EmailVerification.create(
                uid = uid,
                email = email,
                emailVerificationType = emailVerificationType
            )
            return emailVerificationRepository.save(emailVerification)
        }
    }
}
