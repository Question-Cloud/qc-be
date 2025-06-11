package com.eager.questioncloud.application.api.user.register.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.register.dto.CreateUserRequest
import com.eager.questioncloud.application.api.user.register.dto.CreateUserResponse
import com.eager.questioncloud.application.api.user.register.service.RegisterUserService
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/register")
class RegisterUserController(
    private val registerUserService: RegisterUserService
) {
    @PostMapping
    fun createUser(@RequestBody request: @Valid CreateUserRequest): CreateUserResponse {
        val user = registerUserService.create(request.toCreateUser())
        val emailVerification = registerUserService.sendCreateUserVerifyMail(user)
        return CreateUserResponse(emailVerification.resendToken)
    }

    @GetMapping("/resend-verification-mail")
    fun resendVerificationMail(@RequestParam resendToken: String): DefaultResponse {
        registerUserService.resend(resendToken)
        return success()
    }

    @GetMapping("/verify")
    fun verifyCreateUser(@RequestParam token: String): DefaultResponse {
        registerUserService.verifyCreateUser(token, EmailVerificationType.CreateUser)
        return success()
    }
}
