package com.eager.questioncloud.user.account.service

import com.eager.questioncloud.common.mail.EmailSender
import com.eager.questioncloud.user.account.implement.UserAccountUpdater
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.implement.EmailVerificationProcessor
import com.eager.questioncloud.user.repository.UserRepository
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
        emailSender.send(emailVerification.toEmail())
    }
    
    fun sendChangePasswordMail(userId: Long) {
        val user = userRepository.getUser(userId)
        val emailVerification = emailVerificationProcessor.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.ChangePassword
        )
        emailSender.send(emailVerification.toEmail())
    }
    
    fun changePassword(token: String, newPassword: String) {
        val emailVerification = emailVerificationProcessor.verifyEmailVerification(token, EmailVerificationType.ChangePassword)
        userAccountUpdater.changePassword(emailVerification.uid, newPassword)
    }
}
