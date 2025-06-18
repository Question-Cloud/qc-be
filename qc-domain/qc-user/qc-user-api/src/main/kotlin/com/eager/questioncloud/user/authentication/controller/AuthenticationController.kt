package com.eager.questioncloud.user.authentication.controller

import com.eager.questioncloud.user.authentication.dto.LoginRequest
import com.eager.questioncloud.user.authentication.dto.LoginResponse
import com.eager.questioncloud.user.authentication.dto.RefreshResponse
import com.eager.questioncloud.user.authentication.service.AuthenticationService
import com.eager.questioncloud.user.enums.AccountType
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
    fun socialLogin(@RequestParam accountType: AccountType, @RequestParam code: String): LoginResponse {
        val authenticationToken = authenticationService.socialLogin(accountType, code)
        return LoginResponse(authenticationToken)
    }
}
