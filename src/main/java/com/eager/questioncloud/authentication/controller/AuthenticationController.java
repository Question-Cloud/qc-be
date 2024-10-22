package com.eager.questioncloud.authentication.controller;

import com.eager.questioncloud.authentication.dto.Request.LoginRequest;
import com.eager.questioncloud.authentication.dto.Response.LoginResponse;
import com.eager.questioncloud.authentication.dto.Response.RefreshResponse;
import com.eager.questioncloud.authentication.dto.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.authentication.service.AuthenticationService;
import com.eager.questioncloud.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.user.vo.AccountType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "로그인", summary = "로그인", tags = {"auth"}, description = "로그인")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        loginRequest.validate();
        AuthenticationToken authenticationToken = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new LoginResponse(authenticationToken);
    }

    @PostMapping("/refresh")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "토큰 리프레시", summary = "토큰 리프레시", tags = {"auth"}, description = "토큰 리프레시")
    public RefreshResponse refresh(@RequestParam String refreshToken) {
        AuthenticationToken authenticationToken = authenticationService.refresh(refreshToken);
        return new RefreshResponse(authenticationToken);
    }

    @GetMapping("/social")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(
        operationId = "소셜 로그인", summary = "소셜 로그인", tags = {"auth"},
        description = """
                이미 등록되어 있는 소셜 계정이라면 isRegistered: True, authenticationToken이 응답으로 주어집니다.
                등록되어 있지 않은 소셜 계졍이라면 isRegistered: False, registerToken이 응답으로 주어집니다.
                registerToken은 회원가입 시 사용됩니다.
            """
    )
    public SocialAuthenticateResponse socialLogin(@RequestParam AccountType accountType, @RequestParam String code) {
        return authenticationService.socialLogin(accountType, code);
    }
}
