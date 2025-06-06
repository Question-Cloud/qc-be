package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.infrastructure.repository.EmailVerificationRepository
import com.eager.questioncloud.core.domain.verification.model.EmailVerification

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
