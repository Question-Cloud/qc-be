package com.eager.questioncloud.user.scenario

import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.EmailVerificationType

object EmailVerificationScenario {
    fun create(
        user: User,
        emailVerificationType: EmailVerificationType,
    ): EmailVerification {
        return EmailVerification.create(
            uid = user.uid,
            email = user.userInformation.email,
            emailVerificationType = emailVerificationType
        )
    }
}