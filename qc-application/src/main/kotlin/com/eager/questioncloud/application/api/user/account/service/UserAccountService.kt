package com.eager.questioncloud.application.api.user.account.service

import com.eager.questioncloud.application.api.user.account.implement.UserAccountUpdater
import com.eager.questioncloud.application.mail.EmailSender
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor
import com.eager.questioncloud.core.domain.verification.model.Email.Companion.of
import org.springframework.stereotype.Service

@Service
class UserAccountService(
    private val userRepository: UserRepository,
    private val emailVerificationProcessor: EmailVerificationProcessor,
    private val userAccountUpdater: UserAccountUpdater,
    private val emailSender: EmailSender,
) {
    fun recoverForgottenEmail(phone: String): String {
        val user = userRepository.getUserByPhone(phone)
        return user.userInformation.email
    }

    fun sendRecoverForgottenPasswordMail(email: String) {
        val user = userRepository.getUserByEmail(email)
        val emailVerification = emailVerificationProcessor.createEmailVerification(
            user.uid,
            email,
            EmailVerificationType.ChangePassword
        )
        emailSender.sendMail(of(emailVerification))
    }

    fun sendChangePasswordMail(user: User) {
        val emailVerification = emailVerificationProcessor.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.ChangePassword
        )
        emailSender.sendMail(of(emailVerification))
    }

    fun changePassword(token: String, newPassword: String) {
        val emailVerification =
            emailVerificationProcessor.verifyEmailVerification(token, EmailVerificationType.ChangePassword)
        userAccountUpdater.changePassword(emailVerification.uid, newPassword)
    }
}
