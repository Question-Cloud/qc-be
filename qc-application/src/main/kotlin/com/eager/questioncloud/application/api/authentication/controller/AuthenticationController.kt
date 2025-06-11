package com.eager.questioncloud.application.api.authentication.controller

import com.eager.questioncloud.application.api.authentication.dto.LoginRequest
import com.eager.questioncloud.application.api.authentication.dto.LoginResponse
import com.eager.questioncloud.application.api.authentication.dto.RefreshResponse
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticateResponse
import com.eager.questioncloud.application.api.authentication.service.AuthenticationService
import com.eager.questioncloud.core.domain.user.enums.AccountType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val authenticationToken = authenticationService.login(loginRequest.email, loginRequest.password)
        return LoginResponse(authenticationToken)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestParam refreshToken: String): RefreshResponse {
        val authenticationToken = authenticationService.refresh(refreshToken)
        return RefreshResponse(authenticationToken)
    }

    @GetMapping("/social")
    fun socialLogin(@RequestParam accountType: AccountType, @RequestParam code: String): SocialAuthenticateResponse {
        val socialAuthenticationResult = authenticationService.socialLogin(accountType, code)
        return SocialAuthenticateResponse.create(socialAuthenticationResult)
    }
}
