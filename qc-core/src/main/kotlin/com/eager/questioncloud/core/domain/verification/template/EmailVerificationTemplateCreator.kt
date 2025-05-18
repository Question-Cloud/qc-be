package com.eager.questioncloud.core.domain.verification.template

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error

object EmailVerificationTemplateCreator {
    fun getTemplate(emailVerificationType: EmailVerificationType, token: String): EmailVerificationTemplate {
        return when (emailVerificationType) {
            EmailVerificationType.CreateUser -> {
                CreateUserEmailVerificationTemplate(token)
            }

            EmailVerificationType.ChangePassword -> {
                ChangePasswordEmailVerificationTemplate(token)
            }

            else -> {
                throw CoreException(Error.INTERNAL_SERVER_ERROR)
            }
        }
    }
}
