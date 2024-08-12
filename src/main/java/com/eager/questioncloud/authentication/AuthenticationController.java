package com.eager.questioncloud.authentication;

import com.eager.questioncloud.social.SocialAuthenticateProcessor;
import com.eager.questioncloud.user.AccountType;
import com.eager.questioncloud.user.CreateSocialUserInformation;
import com.eager.questioncloud.user.CreateUserService;
import com.eager.questioncloud.user.Request.LoginRequest;
import com.eager.questioncloud.user.Response.LoginResponse;
import com.eager.questioncloud.user.Response.RefreshResponse;
import com.eager.questioncloud.user.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.user.User;
import com.eager.questioncloud.user.UserService;
import java.util.Optional;
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
    private final SocialAuthenticateProcessor socialAuthenticateProcessor;
    private final AuthenticationService authenticationService;
    private final CreateUserService createUserService;
    private final UserService userService;

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
        String socialUid = socialAuthenticateProcessor.getSocialUid(accountType, code);
        Optional<User> socialUser = userService.getSocialUser(accountType, socialUid);
        if (socialUser.isPresent()) {
            socialUser.get().checkUserStatus();
            return new SocialAuthenticateResponse(true, null, authenticationService.generateAuthenticateToken(socialUser.get().getUid()));
        }

        CreateSocialUserInformation createSocialUserInformation = createUserService.createSocialUserInformation(
            CreateSocialUserInformation.create(accountType, socialUid));
        return new SocialAuthenticateResponse(false, createSocialUserInformation.getRegisterToken(), null);
    }
}
