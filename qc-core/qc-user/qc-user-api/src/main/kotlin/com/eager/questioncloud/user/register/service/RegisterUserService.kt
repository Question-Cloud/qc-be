package com.eager.questioncloud.user.register.service

import com.eager.questioncloud.common.mail.EmailSender
import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.implement.EmailVerificationProcessor
import com.eager.questioncloud.user.register.implement.UserActivator
import com.eager.questioncloud.user.register.implement.UserRegister
import org.springframework.stereotype.Component

@Component
class RegisterUserService(
    private val emailVerificationProcessor: EmailVerificationProcessor,
    private val userActivator: UserActivator,
    private val emailSender: EmailSender,
    private val userRegister: UserRegister,
) {
    fun register(createUser: CreateUser): EmailVerification {
        val user = userRegister.create(createUser)
        val emailVerification = emailVerificationProcessor.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.CreateUser
        )
        emailSender.send(emailVerification.toEmail())
        return emailVerification
    }
    
    fun resend(resendToken: String) {
        val emailVerification = emailVerificationProcessor.getByResendToken(resendToken)
        emailSender.send(emailVerification.toEmail())
    }
    
    fun verifyCreateUser(token: String, emailVerificationType: EmailVerificationType) {
        val emailVerification = emailVerificationProcessor.verifyEmailVerification(token, emailVerificationType)
        userActivator.activate(emailVerification.uid)
    }
}
