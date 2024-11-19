package com.eager.questioncloud.application.user;

import com.eager.questioncloud.application.common.DefaultResponse;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.application.user.UserAccountControllerResponse.RecoverForgottenEmailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/account")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping("/recover/email")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "계정 찾기", summary = "계정 찾기", tags = {"help"}, description = "계정 찾기")
    public RecoverForgottenEmailResponse recoverForgottenEmail(@RequestParam String phone) {
        String email = userAccountService.recoverForgottenEmail(phone);
        return new RecoverForgottenEmailResponse(email);
    }

    @GetMapping("/recover/password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "비밀번호 찾기", summary = "비밀번호 찾기", tags = {"help"}, description = "비밀번호 찾기")
    public DefaultResponse sendRecoverForgottenPasswordMail(@RequestParam String email) {
        userAccountService.sendRecoverForgottenPasswordMail(email);
        return DefaultResponse.success();
    }

    @GetMapping("/change-password-mail")
    public DefaultResponse requestChangePasswordMail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userAccountService.sendChangePasswordMail(userPrincipal.getUser());
        return new DefaultResponse(true);
    }

    @PostMapping("/change-password")
    public DefaultResponse changePassword(@RequestBody @Valid UserAccountControllerRequest.ChangePasswordRequest changePasswordRequest) {
        userAccountService.changePassword(changePasswordRequest.getToken(), changePasswordRequest.getNewPassword());
        return DefaultResponse.success();
    }
}
