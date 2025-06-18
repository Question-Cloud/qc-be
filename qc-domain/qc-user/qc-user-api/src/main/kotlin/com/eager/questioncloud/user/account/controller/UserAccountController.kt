package com.eager.questioncloud.user.account.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.user.account.dto.ChangePasswordRequest
import com.eager.questioncloud.user.account.dto.RecoverForgottenEmailResponse
import com.eager.questioncloud.user.account.service.UserAccountService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/account")
class UserAccountController(
    private val userAccountService: UserAccountService
) {
    @GetMapping("/recover/email")
    fun recoverForgottenEmail(@RequestParam phone: String): RecoverForgottenEmailResponse {
        val email = userAccountService.recoverForgottenEmail(phone)
        return RecoverForgottenEmailResponse(email)
    }

    @GetMapping("/recover/password")
    fun sendRecoverForgottenPasswordMail(@RequestParam email: String): DefaultResponse {
        userAccountService.sendRecoverForgottenPasswordMail(email)
        return DefaultResponse.success()
    }

    @GetMapping("/change-password-mail")
    fun requestChangePasswordMail(userPrincipal: UserPrincipal): DefaultResponse {
        userAccountService.sendChangePasswordMail(userPrincipal.userId)
        return DefaultResponse(true)
    }

    @PostMapping("/change-password")
    fun changePassword(@RequestBody changePasswordRequest: @Valid ChangePasswordRequest): DefaultResponse {
        userAccountService.changePassword(changePasswordRequest.token, changePasswordRequest.newPassword)
        return DefaultResponse.success()
    }
}
