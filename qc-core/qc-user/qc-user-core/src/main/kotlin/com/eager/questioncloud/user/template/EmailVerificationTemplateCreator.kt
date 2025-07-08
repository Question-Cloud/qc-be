package com.eager.questioncloud.user.template

import com.eager.questioncloud.user.enums.EmailVerificationType

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
