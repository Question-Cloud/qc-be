package com.eager.questioncloud.user.account.service

import com.eager.questioncloud.common.mail.EmailSender
import com.eager.questioncloud.user.account.implement.UserAccountUpdater
import com.eager.questioncloud.user.common.implement.UserFinder
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.implement.EmailVerificationProcessor
import org.springframework.stereotype.Service

@Service
class UserAccountService(
    private val userFinder: UserFinder,
    private val userAccountUpdater: UserAccountUpdater,
    private val emailVerificationProcessor: EmailVerificationProcessor,
    private val emailSender: EmailSender,
) {
    fun recoverForgottenEmail(phone: String): String {
        val user = userFinder.getByPhone(phone)
        return user.userInformation.email
    }
    
    fun sendRecoverForgottenPasswordMail(email: String) {
        val user = userFinder.getByEmail(email)
        val emailVerification = emailVerificationProcessor.createEmailVerification(user.uid, email, EmailVerificationType.ChangePassword)
        emailSender.send(emailVerification.toEmail())
    }
    
    fun sendChangePasswordMail(userId: Long) {
        val user = userFinder.getById(userId)
        val emailVerification =
            emailVerificationProcessor.createEmailVerification(user.uid, user.userInformation.email, EmailVerificationType.ChangePassword)
        emailSender.send(emailVerification.toEmail())
    }
    
    fun changePassword(token: String, newPassword: String) {
        val emailVerification = emailVerificationProcessor.verifyEmailVerification(token, EmailVerificationType.ChangePassword)
        userAccountUpdater.changePassword(emailVerification.uid, newPassword)
    }
}
