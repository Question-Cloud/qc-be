package com.eager.questioncloud.authentication;

import com.eager.questioncloud.user.AccountType;
import com.eager.questioncloud.user.Request.LoginRequest;
import com.eager.questioncloud.user.Response.LoginResponse;
import com.eager.questioncloud.user.Response.RefreshResponse;
import com.eager.questioncloud.user.Response.SocialAuthenticateResponse;
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
        AuthenticationToken authenticationToken = authenticationService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        return new LoginResponse(authenticationToken);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestParam String refreshToken) {
        AuthenticationToken authenticationToken = authenticationService.refresh(refreshToken);
        return new RefreshResponse(authenticationToken);
    }

    @GetMapping("/social")
    public SocialAuthenticateResponse socialLogin(@RequestParam AccountType accountType, @RequestParam String code) {
        return authenticationService.socialLogin(accountType, code);
    }
}
