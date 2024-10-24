package com.eager.questioncloud.api.user;

import com.eager.questioncloud.api.user.Response.MyInformationResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.user.dto.MyInformation;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import com.eager.questioncloud.core.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public MyInformationResponse getMyInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new MyInformationResponse(MyInformation.from(userPrincipal.getUser()));
    }

    @PatchMapping("/me")
    public DefaultResponse updateMyInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid Request.UpdateMyInformationRequest request) {
        userService.updateUser(userPrincipal.getUser(), request.getName(), request.getProfileImage());
        return DefaultResponse.success();
    }

    @GetMapping("/change-password-mail")
    public DefaultResponse requestChangePasswordMail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.sendChangePasswordMail(userPrincipal.getUser());
        return new DefaultResponse(true);
    }

    @PostMapping("/change-password")
    public DefaultResponse changePassword(@RequestBody @Valid Request.ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest.getToken(), changePasswordRequest.getNewPassword());
        return DefaultResponse.success();
    }
}
