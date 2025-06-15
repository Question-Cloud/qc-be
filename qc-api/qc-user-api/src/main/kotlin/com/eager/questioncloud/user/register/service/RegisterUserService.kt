package com.eager.questioncloud.user.register.service

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.user.mail.EmailSender
import com.eager.questioncloud.user.register.implement.UserRegister
import com.eager.questioncloud.verification.domain.Email
import com.eager.questioncloud.verification.domain.EmailVerification
import com.eager.questioncloud.verification.enums.EmailVerificationType
import com.eager.questioncloud.verification.implement.EmailVerificationProcessor
import org.springframework.stereotype.Component

@Component
class RegisterUserService(
    private val emailVerificationProcessor: EmailVerificationProcessor,
    private val userRepository: UserRepository,
    private val emailSender: EmailSender,
    private val userRegister: UserRegister,
) {
    fun create(createUser: CreateUser): User {
        return userRegister.create(createUser)
    }

    fun sendCreateUserVerifyMail(user: User): EmailVerification {
        val emailVerification = emailVerificationProcessor.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.CreateUser
        )
        emailSender.sendMail(Email.of(emailVerification))
        return emailVerification
    }

    fun resend(resendToken: String) {
        val emailVerification = emailVerificationProcessor.getByResendToken(resendToken)
        emailSender.sendMail(Email.of(emailVerification))
    }

    fun verifyCreateUser(token: String, emailVerificationType: EmailVerificationType) {
        val emailVerification = emailVerificationProcessor.verifyEmailVerification(token, emailVerificationType)
        val user = userRepository.getUser(emailVerification.uid)
        user.active()
        userRepository.save(user)
    }
}
