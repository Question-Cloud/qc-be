package com.eager.questioncloud.help.controller;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.help.dto.Response.RecoverForgottenEmailResponse;
import com.eager.questioncloud.help.service.HelpAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
public class HelpAccountController {
    private final HelpAccountService helpAccountService;

    @GetMapping("/recover/email")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "계정 찾기", summary = "계정 찾기", tags = {"help"}, description = "계정 찾기")
    public RecoverForgottenEmailResponse recoverForgottenEmail(@RequestParam String phone) {
        String email = helpAccountService.recoverForgottenEmail(phone);
        return new RecoverForgottenEmailResponse(email);
    }

    @GetMapping("/recover/password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "비밀번호 찾기", summary = "비밀번호 찾기", tags = {"help"}, description = "비밀번호 찾기")
    public DefaultResponse sendRecoverForgottenPasswordMail(@RequestParam String email) {
        helpAccountService.sendRecoverForgottenPasswordMail(email);
        return DefaultResponse.success();
    }
}
