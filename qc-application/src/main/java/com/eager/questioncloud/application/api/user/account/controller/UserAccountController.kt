package com.eager.questioncloud.application.api.user.account.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.account.dto.ChangePasswordRequest
import com.eager.questioncloud.application.api.user.account.dto.RecoverForgottenEmailResponse
import com.eager.questioncloud.application.api.user.account.service.UserAccountService
import com.eager.questioncloud.application.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/account")
class UserAccountController(
    private val userAccountService: UserAccountService
) {
    @GetMapping("/recover/email")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "계정 찾기", summary = "계정 찾기", tags = ["help"], description = "계정 찾기")
    fun recoverForgottenEmail(@RequestParam phone: String): RecoverForgottenEmailResponse {
        val email = userAccountService.recoverForgottenEmail(phone)
        return RecoverForgottenEmailResponse(email)
    }

    @GetMapping("/recover/password")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "비밀번호 찾기", summary = "비밀번호 찾기", tags = ["help"], description = "비밀번호 찾기")
    fun sendRecoverForgottenPasswordMail(@RequestParam email: String): DefaultResponse {
        userAccountService.sendRecoverForgottenPasswordMail(email)
        return success()
    }

    @GetMapping("/change-password-mail")
    fun requestChangePasswordMail(@AuthenticationPrincipal userPrincipal: UserPrincipal): DefaultResponse {
        userAccountService.sendChangePasswordMail(userPrincipal.user)
        return DefaultResponse(true)
    }

    @PostMapping("/change-password")
    fun changePassword(@RequestBody changePasswordRequest: @Valid ChangePasswordRequest): DefaultResponse {
        userAccountService.changePassword(changePasswordRequest.token, changePasswordRequest.newPassword)
        return success()
    }
}
