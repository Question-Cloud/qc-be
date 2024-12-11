package com.eager.questioncloud.application.api.user.profile.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.user.profile.dto.UserPointControllerRequest.UpdateMyInformationRequest;
import com.eager.questioncloud.application.api.user.profile.dto.UserPointControllerResponse.MyInformationResponse;
import com.eager.questioncloud.application.api.user.profile.service.UserProfileService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.domain.user.dto.MyInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userService;

    @GetMapping("/me")
    public MyInformationResponse getMyInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new MyInformationResponse(MyInformation.from(userPrincipal.getUser()));
    }

    @PatchMapping("/me")
    public DefaultResponse updateMyInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateMyInformationRequest request) {
        userService.updateUserInformation(userPrincipal.getUser(), request.getName(), request.getProfileImage());
        return DefaultResponse.success();
    }
}
