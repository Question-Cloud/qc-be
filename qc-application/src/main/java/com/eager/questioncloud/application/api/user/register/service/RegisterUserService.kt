package com.eager.questioncloud.application.api.user.register.service

import com.eager.questioncloud.application.api.user.register.implement.UserRegister
import com.eager.questioncloud.application.mail.EmailSender
import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor
import com.eager.questioncloud.core.domain.verification.model.Email.Companion.of
import com.eager.questioncloud.core.domain.verification.model.EmailVerification
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
            user.uid!!,
            user.userInformation.email,
            EmailVerificationType.CreateUser
        )
        emailSender.sendMail(of(emailVerification))
        return emailVerification
    }

    fun resend(resendToken: String) {
        val emailVerification = emailVerificationProcessor.getByResendToken(resendToken)
        emailSender.sendMail(of(emailVerification))
    }

    fun verifyCreateUser(token: String, emailVerificationType: EmailVerificationType) {
        val emailVerification = emailVerificationProcessor.verifyEmailVerification(token, emailVerificationType)
        val user = userRepository.getUser(emailVerification.uid)
        user.active()
        userRepository.save(user)
    }
}
