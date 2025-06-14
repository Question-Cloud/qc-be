package com.eager.questioncloud.verification.template

import com.eager.questioncloud.verification.enums.EmailVerificationType

object EmailVerificationTemplateCreator {
    fun getTemplate(
        emailVerificationType: EmailVerificationType,
        token: String
    ): EmailVerificationTemplate {
        return when (emailVerificationType) {
            EmailVerificationType.CreateUser -> {
                CreateUserEmailVerificationTemplate(token)
            }

            EmailVerificationType.ChangePassword -> {
                ChangePasswordEmailVerificationTemplate(token)
            }
        }
    }
}
