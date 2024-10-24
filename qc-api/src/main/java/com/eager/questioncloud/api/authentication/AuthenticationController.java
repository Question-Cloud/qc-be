package com.eager.questioncloud.api.authentication;

import com.eager.questioncloud.api.authentication.Request.LoginRequest;
import com.eager.questioncloud.api.authentication.Response.LoginResponse;
import com.eager.questioncloud.api.authentication.Response.RefreshResponse;
import com.eager.questioncloud.api.authentication.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.core.domain.authentication.service.AuthenticationService;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.core.domain.social.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
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
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        AuthenticationToken authenticationToken = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new LoginResponse(authenticationToken);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestParam String refreshToken) {
        AuthenticationToken authenticationToken = authenticationService.refresh(refreshToken);
        return new RefreshResponse(authenticationToken);
    }

    @GetMapping("/social")
    public SocialAuthenticateResponse socialLogin(@RequestParam AccountType accountType, @RequestParam String code) {
        SocialAuthenticateResult socialAuthenticateResult = authenticationService.socialLogin(accountType, code);
        return SocialAuthenticateResponse.create(socialAuthenticateResult);
    }
}
