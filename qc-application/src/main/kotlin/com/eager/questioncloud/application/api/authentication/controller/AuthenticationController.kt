package com.eager.questioncloud.application.api.authentication.controller

import com.eager.questioncloud.application.api.authentication.dto.LoginRequest
import com.eager.questioncloud.application.api.authentication.dto.LoginResponse
import com.eager.questioncloud.application.api.authentication.dto.RefreshResponse
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticateResponse
import com.eager.questioncloud.application.api.authentication.service.AuthenticationService
import com.eager.questioncloud.core.domain.user.enums.AccountType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "로그인 성공")])
    @Operation(operationId = "로그인", summary = "로그인", tags = ["authentication"], description = "이메일/비밀번호 로그인")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val authenticationToken = authenticationService.login(loginRequest.email, loginRequest.password)
        return LoginResponse(authenticationToken)
    }

    @PostMapping("/refresh")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "토큰 갱신 성공")])
    @Operation(operationId = "토큰 리프레시", summary = "토큰 리프레시", tags = ["authentication"], description = "리프레시 토큰으로 액세스 토큰 갱신")
    fun refresh(@RequestParam refreshToken: String): RefreshResponse {
        val authenticationToken = authenticationService.refresh(refreshToken)
        return RefreshResponse(authenticationToken)
    }

    @GetMapping("/social")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "소셜 로그인 성공")])
    @Operation(operationId = "소셜 로그인", summary = "소셜 로그인", tags = ["authentication"], description = "소셜 계정으로 로그인")
    fun socialLogin(@RequestParam accountType: AccountType, @RequestParam code: String): SocialAuthenticateResponse {
        val socialAuthenticationResult = authenticationService.socialLogin(accountType, code)
        return SocialAuthenticateResponse.create(socialAuthenticationResult)
    }
}
