package com.eager.questioncloud.user.register.controller

import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.register.dto.CreateUserRequest
import com.eager.questioncloud.user.register.dto.CreateUserResponse
import com.eager.questioncloud.user.register.service.RegisterUserService
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
        return DefaultResponse.success()
    }

    @GetMapping("/verify")
    fun verifyCreateUser(@RequestParam token: String): DefaultResponse {
        registerUserService.verifyCreateUser(token, EmailVerificationType.CreateUser)
        return DefaultResponse.success()
    }
}
