package com.eager.questioncloud.user;

import com.eager.questioncloud.user.Response.SocialAuthenticateResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/social")
@RequiredArgsConstructor
public class UserSocialAuthController {
    private final UserSocialService userSocialService;
    private final UserService userService;

    @GetMapping
    public SocialAuthenticateResponse socialAuth(@RequestParam AccountType accountType, @RequestParam String code) {
        String socialUid = userSocialService.getSocialUid(accountType, code);
        Optional<User> socialUser = userService.getSocialUser(accountType, socialUid);
        if (socialUser.isPresent()) {
            return new SocialAuthenticateResponse(true, null, "accessToken", "refreshToken");
        } else {
            return new SocialAuthenticateResponse(false, "registerToken", null, null);
        }
    }
}
