package com.eager.questioncloud.application.api.user.register.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.register.dto.CreateUserRequest
import com.eager.questioncloud.application.api.user.register.dto.CreateUserResponse
import com.eager.questioncloud.application.api.user.register.service.RegisterUserService
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/register")
class RegisterUserController(
    private val registerUserService: RegisterUserService
) {
    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "회원가입", summary = "회원가입", tags = ["user-register"], description = "회원가입")
    fun createUser(@RequestBody request: @Valid CreateUserRequest): CreateUserResponse {
        val user = registerUserService.create(request.toCreateUser())
        val emailVerification = registerUserService.sendCreateUserVerifyMail(user)
        return CreateUserResponse(emailVerification.resendToken)
    }

    @GetMapping("/resend-verification-mail")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "회원가입 인증 메일 재요청",
        summary = "회원가입 인증 메일 재요청",
        tags = ["user-register"],
        description = "회원가입 인증 메일 재요청"
    )
    fun resendVerificationMail(@RequestParam resendToken: String): DefaultResponse {
        registerUserService.resend(resendToken)
        return success()
    }

    @GetMapping("/verify")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "회원가입 인증 메일 확인",
        summary = "회원가입 인증 메일 확인",
        tags = ["user-register"],
        description = "회원가입 인증 메일 확인"
    )
    fun verifyCreateUser(@RequestParam token: String): DefaultResponse {
        registerUserService.verifyCreateUser(token, EmailVerificationType.CreateUser)
        return success()
    }
}
