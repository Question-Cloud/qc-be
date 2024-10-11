package com.eager.questioncloud.user.controller;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.security.UserPrincipal;
import com.eager.questioncloud.user.dto.Request.ChangePasswordRequest;
import com.eager.questioncloud.user.dto.Request.UpdateMyInformationRequest;
import com.eager.questioncloud.user.dto.Response.MyInformationResponse;
import com.eager.questioncloud.user.dto.UserDto.MyInformation;
import com.eager.questioncloud.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "내 정보 조회", summary = "내 정보 조회", tags = {"user"}, description = "내 정보 조회")
    public MyInformationResponse getMyInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new MyInformationResponse(MyInformation.of(userPrincipal.getUser()));
    }

    @PatchMapping("/me")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "내 정보 수정", summary = "내 정보 수정", tags = {"user"}, description = "내 정보 수정")
    public DefaultResponse updateMyInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateMyInformationRequest request) {
        userService.updateUser(userPrincipal.getUser(), request.getName(), request.getProfileImage());
        return DefaultResponse.success();
    }

    @GetMapping("/change-password-mail")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "비밀번호 변경 메일 요청", summary = "비밀번호 변경 메일 요청", tags = {"user"}, description = "비밀번호 변경 메일 요청")
    public DefaultResponse requestChangePasswordMail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.sendChangePasswordMail(userPrincipal.getUser());
        return new DefaultResponse(true);
    }

    @PostMapping("/change-password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "비밀번호 변경", summary = "비밀번호 변경", tags = {"user"}, description = "비밀번호 변경")
    public DefaultResponse changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest.getToken(), changePasswordRequest.getNewPassword());
        return DefaultResponse.success();
    }
}
